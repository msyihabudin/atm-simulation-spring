package com.syhb.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);

		try {
			Scanner scan = new Scanner(System.in);
			System.out.print("Masukkan Nomor Rekening: ");
			String accountNumber = scan.nextLine();
			while (true) {
				System.out.println("\nMasukkan 6-Digit PIN Anda atau tekan 'X' untuk keluar: ");
				String pinNumber = scan.nextLine();
				MainApp mainApp = new MainApp(accountNumber, pinNumber, false);
				mainApp.main();

				if (mainApp.isClose() || pinNumber.toLowerCase().equals("x")) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
