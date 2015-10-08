CLASSPATH = .:/usr/share/java/RXTXcomm.jar 
JFLAGS = -classpath $(CLASSPATH) 
JCFLAGS = 
JRFLAGS = -Djava.library.path=".:/usr/lib/jni/"
JAVAC = javac $(JFLAGS) $(JCFLAGS) 
JAVA = sudo java $(JFLAGS) $(JRFLAGS) 

MAIN = SerialComm
# 列出所有要編譯的 java 檔, 全部放到一個變數代替 
JAVASRC = $(shell ls *.java) 
#JAVASRC = SerialComm.java

# 替換規則: 把 JAVASRC 列出結果的所有 .java 換成 .class 
CLASSES = $(JAVASRC:.java=.class) 
JDK = /opt/java/64/jdk1.7.0_51/ 


# 通用規則: java -> class 表示 .class 永遠 depend on .java 的意思 
# 在後面的 target 中, 若有 class 變動, 就會預設的執行 下面得 javac 命令 
# 例如: 
# compile : $(CLASSES) 
# see example: http://myweb.stedwards.edu/laurab/help/javamakefile.html 
# 
#也可以寫成 $* 為所有 depend 的檔名 (不含副檔名) 
#.java.class: 
#[tab鍵]$(JAVAC) $(JFLAGS) $*.java 
.SUFFIXES : .java .class 
.java.class : 
	$(JAVAC) $< 
# 目標文件 加上 所有比目標文件新的 depend 檔 (ex: javac -o test.class test.java) 

all : build run 
	
# build depend on $(CLASSES) 所有檔案 
# 只要 $(CLASSES) 有檔案變化, 則會自動執行 .java.class 的預設命令 
build: $(CLASSES) 
	
run: $(CLASSES)
	$(JAVA) $(MAIN)

# 清除命令 
clean: 
	rm -f *.class  
#sudo java -Djava.library.path=".:/usr/lib/jni/" -cp /usr/share/java/RXTXcomm.jar:. SerialComm
#javac -cp "/usr/share/java/RXTXcomm.jar" SerialComm.java
