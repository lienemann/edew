CLASSES = JBufferedWriter.class ConnectStdInOut.class Evolverwin.class Prefwin.class Simulation.class Sim1DPath.class Sim2DArray.class SimActSch.class SimR4Channel.class SimRamp_mult.class SimRamp_one.class Mainwin.class Simtool.class SimSpikeShape.class
Simtool.jar: $(CLASSES) MANIFEST.MF
	jar cvmf MANIFEST.MF Simtool.jar *.class

Sim1DPath.class: Simulation.class
Sim2DArray.class: Simulation.class
SimActSch.class: Simulation.class
SimR4Channel.class: Simulation.class
SimRamp_mult.class: Simulation.class
SimRamp_one.class: Simulation.class
SimSpikeShape.class: Simulation.class
Mainwin.class: Simulation.class Prefwin.class
Simtool.class: Mainwin.class
Evolverwin.class: ConnectStdInOut.class

$(CLASSES): %.class: %.java
	javac -classpath . $<

run: $(CLASSES)
	java -classpath . Simtool

runjar: Simtool.jar
	java -jar Simtool.jar

.PHONY: clean
clean:
	-rm -v *.class Simtool.jar
