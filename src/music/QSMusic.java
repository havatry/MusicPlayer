package music;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class QSMusic extends JFrame implements ActionListener{
	/** serialVersionUID*/
	private static final long serialVersionUID = 1L;
	private File[] files;
	private Player player;
	private Thread thread;
	private JButton jbtPre=new JButton("上一歌曲");
	private JButton jbtNext=new JButton("下一歌曲");
	private JButton jbtRandom=new JButton("随机播放");
	private JButton jbtLoop=new JButton("循环播放");
	private JButton jbtNormal=new JButton("正常播放");
	private JButton jbtStop=new JButton("停止播放");
	private JLabel label=new JLabel();
	private int z=0;
	private int mode=1;
	private int prez=0;
	private final static int PREMUSIC=0;
	private final static int NEXTMUSIC=1;
	private final static int RANDOMMODE=1;
	private final static int LOOPMODE=2;
	private final static int NORMALMODE=4;
	public QSMusic() {
		// TODO Auto-generated constructor stub
	}
	
	public QSMusic(String musicFolder){
		jbtPre.addActionListener(this);
		jbtNext.addActionListener(this);
		jbtRandom.addActionListener(this);
		jbtLoop.addActionListener(this);
		jbtNormal.addActionListener(this);
		jbtStop.addActionListener(this);
		jbtPre.setBackground(new Color(238,238,238));
		jbtNext.setBackground(new Color(238,238,238));
		jbtRandom.setBackground(new Color(238,238,238));
		jbtLoop.setBackground(new Color(238,238,238));
		jbtNormal.setBackground(new Color(238,238,238));
		jbtStop.setBackground(new Color(238,238,238));
		File[] myfiles=new File(musicFolder).listFiles();
		ArrayList<File> filelist=new ArrayList<>(Arrays.asList(myfiles));
		Collections.shuffle(filelist);
		files=new File[filelist.size()];
		for(int i=0;i<filelist.size();i++)
			files[i]=filelist.get(i);
		label.setText("随机播放歌曲: "+files[0].getName());
		mode=RANDOMMODE;
		thread=new AudioPlayer();
		thread.start();
		add(label,BorderLayout.NORTH);
		add(new Panele(),BorderLayout.CENTER);
	}
	
	private class Panele extends JPanel{
		/** serialVersionUID*/
		private static final long serialVersionUID = 1L;

		public Panele() {
			// TODO Auto-generated constructor stub
			setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
			add(jbtPre);
			add(jbtNext);
			add(jbtNormal);
			add(jbtLoop);
			add(jbtRandom);
			add(jbtStop);
		}
	}

	@Override
	public void paintComponents(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponents(g);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command=e.getActionCommand();
		switch (command) {
		case "上一歌曲":
			whichToPlay(PREMUSIC, mode);
			thread.stop();
			thread=new AudioPlayer();
			thread.start();
			break;
		case "下一歌曲":
			whichToPlay(NEXTMUSIC, mode);
			thread.stop();
			thread=new AudioPlayer();
			thread.start();
			break;
		case "循环播放":
			mode=LOOPMODE;
			break;
		case "随机播放":
			mode=RANDOMMODE;
			break;
		case "正常播放":
			mode=NORMALMODE;
			break;
		case "停止播放":
			thread.stop();
			break;
		default:
			break;
		}
		setLabel();
	}
	
	private void setLabel() {
		String tmp="";
		if(mode==NORMALMODE)
			tmp="正常";
		else if(mode==RANDOMMODE)
			tmp="随机";
		else {
			tmp="循环";
		}
		label.setText(tmp+"播放歌曲: "+files[z].getName());
	}
	
	private void whichToPlay(int function,int mode) {
		if(mode==NORMALMODE){
			if(function==PREMUSIC)
				z=prez;
			else if(function==NEXTMUSIC){
				prez=z;
				z=(++z)%files.length;
			}
		}else if(mode==RANDOMMODE){
			if(function==NEXTMUSIC){
				prez=z;
				int tmp=(int)(Math.random()*files.length);
				while(tmp==prez){
					//保证每次不一样的歌曲
					tmp=(int)(Math.random()*files.length);
				}
				z=tmp;
			}else{
				z=prez;
			}
		}
	}
	
	private void normalPlay(int mode) {
		prez=z;
		if(mode==NORMALMODE){
			z++;
		}
		else if(mode==RANDOMMODE){
			int tmp=(int)(Math.random()*files.length);
			while(tmp==prez){
				//保证每次不一样的歌曲
				tmp=(int)(Math.random()*files.length);
			}
			z=tmp;
		}
		setLabel();
	}
	
	private class AudioPlayer extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				while(true){
					play();
					normalPlay(mode);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		private void play() throws FileNotFoundException,JavaLayerException{
			BufferedInputStream inputStream=new BufferedInputStream(
					new FileInputStream(files[z]));
			player=new Player(inputStream);
			repaint();
			player.play();
		}
	}
	
	public static void main(String[] args) {
		JFrame frame=new QSMusic("music");
		frame.setTitle("青山播放器");
		frame.setSize(248,168);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
}
