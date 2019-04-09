# MusicPlayer
简单实现音乐播放器

使用说明
1.命令行执行
```bash
cd MusicPlayer
javac -Djava.ext.dirs=lib -d bin src/music/*.java
java -classpath bin -Djava.ext.dirs=lib music.QSMusic
```

2.jar包使用
```bash
cd MusicPlayer
echo -e "Main-Class: music.QSMusic\nClass-Path: lib/jl1.0.1.jar" > manf
jar cvfm 青山播放器.jar manf -C bin/ music/QSMusic.class bin/music/* music/*
java -jar 青山播放器.jar
```