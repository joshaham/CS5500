.PHONY: clean hoshua moses zhuoli
default:
	javac zhuoli/*.java
	javac joshua/*.java
	javac moses/*.java
run:
	java -cp ./zhuoli TeamTestProgram zhuoli
	java -cp ./joshua TeamTestProgram joshua
	java -cp ./moses TeamTestProgram moses
clean:
	$(RM) joshua/*.class
	$(RM) moses/*.class
	$(RM) zhuoli/*.class

