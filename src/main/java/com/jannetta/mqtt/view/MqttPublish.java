package com.jannetta.mqtt.view;

import com.jannetta.mqtt.controller.TimerJob;
import org.apache.commons.cli.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

public class MqttPublish {
    private static int period_ms = 60000; // runtime
    private static int delay_ms = 1000; // interval

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
        String port = "1883";

        Options options = new Options();
        Option b = Option.builder("b").hasArg().required().desc("Broker IP address").build();
        Option p = Option.builder("p").hasArg().desc("Port").build();
        Option q = Option.builder("q").hasArg().desc("QoS level").build();
        Option t = Option.builder("t").hasArg().desc("Topic").build();
        Option c = Option.builder("c").hasArg().desc("Client ID").build();
        Option d = Option.builder("d").hasArg().desc("Directory of temperature file").build();
        Option time = Option.builder("time").desc("Publish the time").build();
        Option period = Option.builder("period").hasArg().desc("Period").build();
        Option delay = Option.builder("delay").hasArg().desc("Delay").build();
        options.addOption(b);
        options.addOption(p);
        options.addOption(d);
        options.addOption(q);
        options.addOption(t);
        options.addOption(c);
        options.addOption(time);
        options.addOption(period);
        options.addOption(delay);

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
                content = readValue(dirPath, filename).split(",")[1];
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
            if (cmd.hasOption("time") || !cmd.hasOption("d")) {
                content = LocalDateTime.now().toString();
            }
            if (cmd.hasOption("period")) {
                period_ms = Integer.valueOf(cmd.getOptionValue("period"));
            }
            if (cmd.hasOption("delay")) {
                delay_ms = Integer.valueOf(cmd.getOptionValue("delay"));
            }
            System.out.println("Broker IP: " + broker);
            System.out.println("Port: " + port);
            System.out.println("Data dir: " + dirPath);
            System.out.println("Client id: " + clientId);
            //System.out.println("Persistence: " + persistence.toString());
            System.out.println("Quality of service: " + qos);
            System.out.println("Topic: " + topic);
            System.out.println("Content: " + content);
            TimerTask task = new TimerJob(broker, clientId, persistence, qos, topic, content, port);
            Timer timer = new Timer();
            timer.schedule(task, delay_ms, period_ms);

        } catch (MissingOptionException e) {
            help(options);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    static public String readValue(String dirPath, String file) {
        String lastline = null;
        try {
            Scanner sc = new Scanner(new File(dirPath + "/" + file));
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
        formatter.printHelp("java -cp MitoModel.jar com.jannetta.view.mqtt.MqttPublish\n" + "Version: 1\n"
                + "Publish a mqtt message", options);
    }
}
