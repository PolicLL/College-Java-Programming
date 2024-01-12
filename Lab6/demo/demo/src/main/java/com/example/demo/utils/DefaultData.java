package com.example.demo.utils;

import com.example.demo.model.Address;
import com.example.demo.model.Client;
import com.example.demo.model.Device;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class DefaultData {

    @Getter
    private static List<Address> addresses = new ArrayList<>();

    @Getter
    private static List<Device> devices = new ArrayList<>();

    @Getter
    private static List<Client> clients = new ArrayList<>();

    static {
        resetAll();
    }

    public static void resetAll(){
        for (int i = 0; i < 5; i++) {
            addresses.add(new Address("Street " + (i + 1), "PostalCode " + (i + 1), "State " + (i + 1)));
        }

        for (int i = 0; i < 5; i++) {
            devices.add(new Device("Device " + (i + 1)));
        }

        for (int i = 0; i < 5; i++) {
            devices.get(i).measureConsumptionBefore(100, 1000);
        }

        for (int i = 1; i < 5; i++) {
            Client client = new Client("Client " + (i + 1), addresses.get(i), devices.get(i));
            clients.add(client);
        }
    }

}
