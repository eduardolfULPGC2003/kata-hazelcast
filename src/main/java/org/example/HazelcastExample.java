package org.example;

import com.hazelcast.collection.IList;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.IOException;
import java.util.Arrays;

public class HazelcastExample {
    public static void main(String[] args) throws InterruptedException, IOException {
        Config config = new Config();

        // Fetch the environment variables for mode and member addresses
        String mode = System.getenv("HAZELCAST_MODE");
        String member = System.getenv("HAZELCAST_MEMBER");

        if (mode == null || mode.isBlank()) {
            throw new IllegalArgumentException("HAZELCAST_MODE must be set to either 'INPUT' or 'MONITOR'");
        }

        if (member == null || member.isBlank()) {
            throw new IllegalArgumentException("HAZELCAST_MEMBER must be set with a valid address");
        }

        // Add the primary member (could be self or a predefined one)
        config.getNetworkConfig().getJoin().getTcpIpConfig()
                .setEnabled(true)
                .addMember(member);

        // Fetch all other container IPs in the same Docker network
        String[] additionalMembers = new String[] {"hazelcast-input", "hazelcast-monitor", "eduardo-read"}; // Use service names
        for (String ip : additionalMembers) {
            System.out.println("Adding member: " + ip);
            config.getNetworkConfig().getJoin().getTcpIpConfig().addMember(ip);
        }


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
