.SUFFIXES: .java .class
INDEX ?= ../../indices/tu-no-expansion-nl+uk-stop
TOPICS_LANG ?= uk
DOCK ?= 3
WORDK ?= 8
MULTIPLY ?= true
OUTPUT_DIR ?= ../trash
INPUT_DIR ?= ../commandline-output
JARGS = $(INPUT_DIR)/topics_$(TOPICS_LANG).xml $(DOCK) $(WORDK) $(MULTIPLY) $(INPUT_DIR)/15_$(TOPICS_LANG)_nl-uk_stop_results.trec $(INDEX) $(OUTPUT_DIR)/topics_$(TOPICS_LANG)_$(WORDK).ldf
JMAIN = PRF
LEMUR_DIR=../../lemur-4.12/
CLASSPATH = $(LEMUR_DIR)/swig/obj/java/indri.jar:commons-math3-3.5/commons-math3-3.5.jar:.
JFLAGS = -classpath $(CLASSPATH) -Djava.library.path=$(LEMUR_DIR)/swig/obj/java -Djava.util.logging.config.file=log.properties
# JCFLAGS = -g -classpath $(CLASSPATH)
JCFLAGS = -classpath $(CLASSPATH)
JC = javac
JV = java
.java.class:
	$(JC) $(JCFLAGS) $*.java

CLASSES = \
	QueryReader.java \
	ListFileReader.java \
	QueryWithID.java \
	TrecReader.java \
	QueryResult.java \
	LDFWriter.java \
	LinkedWeightedTerm.java \
	PRF.java
	# DocumentVector.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

run: classes
	$(JV) $(JFLAGS) $(JMAIN) $(JARGS)
	mv warnings.log $(OUTPUT_DIR)/warnings_$(TOPICS_LANG)_$(WORDK).log
