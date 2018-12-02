package com.jannetta.mqtt.view;

import com.jannetta.mqtt.controller.TimerJob;
import org.apache.commons.cli.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MqttPublish {
    private static final int PERIOD_MS = 60000; // runtime
    private static final int DELAY_MS = 1000; // interval

    public static void main(String[] args) {
        String content = "";
        int qos = 0;
        String broker = "";
        String clientId = UUID.randomUUID().toString();
        MemoryPersistence persistence = new MemoryPersistence();
        String topic = "sensor/PC/temperature";
        String dirPath = "";
        String[] file;
        String filename = "";
        String port = ":1883";

        Options options = new Options();
        Option b = Option.builder("b").hasArg().required().desc("Broker IP address").build();
        Option p = Option.builder("p").hasArg().desc("Port").build();
        Option q = Option.builder("q").hasArg().desc("QoS level").build();
        Option t = Option.builder("t").hasArg().desc("Topic").build();
        Option c = Option.builder("c").hasArg().desc("Client ID").build();
        Option d = Option.builder("d").hasArg().required().desc("Directory of temperature file").build();
        options.addOption(b);
        options.addOption(p);
        options.addOption(d);
        options.addOption(q);
        options.addOption(t);
        options.addOption(c);

        CommandLineParser parser = new DefaultParser();

        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("b")) {
                broker = "tcp://" + cmd.getOptionValue("b");
            }
            if (cmd.hasOption("p")) {
                broker = broker + ":" + cmd.getOptionValue("p");
            }
            if (cmd.hasOption("d")) {
                dirPath = cmd.getOptionValue("d");
                file = getDirectory(dirPath);
                filename = file[file.length - 1];
            }
            if (cmd.hasOption("q")) {
                qos = Integer.valueOf(cmd.getOptionValue("q"));
            }
            if (cmd.hasOption("t")) {
                topic = cmd.getOptionValue("t");
            }
            if (cmd.hasOption("c")) {
                clientId = cmd.getOptionValue("c");
            }
            System.out.println("Broker IP: " + broker);
            System.out.println("Data dir: " + dirPath);
            System.out.println("Client id: " + clientId);
            //System.out.println("Persistence: " + persistence.toString());
            System.out.println("Quality of service: " + qos);
            System.out.println("Topic: " + topic);
            content = readValue(dirPath, filename).split(",")[1];
            System.out.println("Content: " + content);
            TimerTask task = new TimerJob(broker, clientId, persistence, qos, topic, content);
            Timer timer = new Timer();
            timer.schedule(task, DELAY_MS, PERIOD_MS);

        } catch (MissingOptionException e) {
            help(options);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    static public String readValue(String dirPath, String file) {
        String lastline = null;
        try {
            Scanner sc = new Scanner(new File(dirPath + file));
            while (sc.hasNextLine()) {
                lastline = sc.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return lastline;
    }

    static public String[] getDirectory(String dirPath) {
        File dir = new File(dirPath);
        String[] files = dir.list();
        if (files.length == 0) {
            System.out.println("The directory is empty");
        } else {
            for (String aFile : files) {
                System.out.println(aFile);
            }
        }
        return files;
    }


    /**
     * Prints the help text explaining command line parameters
     *
     * @param options
     */
    private static void help(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -cp MitoModel.jar com.jannetta.view.CalcMutPredLoad\n" + "Version: 3\n"
                + "Calculate the MutPred load for all haplogrep files specified.", options);
    }
}
