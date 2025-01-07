package org.example;

//run from wsl
// mvn clean package
// HAZELCAST_MODE=INPUT HAZELCAST_MEMBER=127.0.0.1 java -jar target/hazelcast-1.0-SNAPSHOT.jar

import com.hazelcast.collection.IList;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastExample {
    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();

        // Fetch the environment variables for mode and member address
        String mode = System.getenv("HAZELCAST_MODE");
        String member = System.getenv("HAZELCAST_MEMBER");

        if (mode == null || mode.isBlank()) {
            throw new IllegalArgumentException("HAZELCAST_MODE must be set to either 'INPUT' or 'MONITOR'");
        }

        if (member == null || member.isBlank()) {
            throw new IllegalArgumentException("HAZELCAST_MEMBER must be set with a valid address");
        }

        // Configure the TCP/IP join for Hazelcast
        config.getNetworkConfig().getJoin().getTcpIpConfig()
                .setEnabled(true)
                .addMember(member);

        HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
        IList<Integer> lista = instance.getList("lista");

        if (mode.equalsIgnoreCase("INPUT")) {
            // Run as input node
            System.out.println("Running in INPUT mode, adding numbers to the list.");
            int id = 0;
            while (true) {
                System.out.println("Adding ID: " + id);
                lista.add(id++);
                Thread.sleep(2000);
            }
        } else if (mode.equalsIgnoreCase("MONITOR")) {
            // Run as monitor node
            System.out.println("Running in MONITOR mode, monitoring the list size.");
            while (true) {
                System.out.println("Current list size: " + lista.size());
                Thread.sleep(5000);
            }
        } else {
            throw new IllegalArgumentException("Invalid HAZELCAST_MODE. Must be 'INPUT' or 'MONITOR'");
        }
    }
}
