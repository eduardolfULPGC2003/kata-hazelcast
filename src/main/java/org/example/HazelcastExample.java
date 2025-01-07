package org.example;
import com.hazelcast.collection.IList;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.Map;
import java.util.Scanner;


public class HazelcastExample {
    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();

        /* config.getNetworkConfig().getJoin().getTcpIpConfig()
                .setEnabled(true)
                .addMember("192.168.1.138") // Your IP
                .addMember("192.168.1.164") // Jose's IP
                .addMember("192.168.1.165"); // Jose's IP */

        /*config.getNetworkConfig().getInterfaces()
                .setEnabled(true)
                .addInterface("192.168.1.*");*/



        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getTcpIpConfig()
                .setEnabled(true)
                .addMember("192.168.100.2") // Nodo 1
                .addMember("192.168.100.3");

        HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);

        IList<Integer> lista = instance.getList("lista");

        if (Integer.parseInt(args[0])==0){
            int id = 0;
            while (true){
                System.out.println(id);
                lista.add(id++);
                Thread.sleep(2000);
            }
        }

        else {
            while (true){
                System.out.println("Hay "+lista.size()+" elementos en la lista");
                Thread.sleep(5000);
            }
        }

        /*IMap<String, String> map = instance.getMap("distributed-map");
        map.put("user1", "Alice");
        map.put("user2", "Bob");
        System.out.println("User1: " + map.get("user1"));

        Scanner scanner = new Scanner(System.in);
        int user = 3;

        // Bucle infinito con intervalo de 5 segundos entre cada iteración
        while (true) {
            System.out.println("Introduce una palabra para el usuario" + user + ": ");
            String word = scanner.nextLine();
            map.put("user" + user, word);
            user++;

            // Imprimir todas las entradas del mapa
            System.out.println("Entradas en el mapa:");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            // Pausa de 5 segundos (5000 milisegundos)
            Thread.sleep(5000);
        }*/
    }
}

