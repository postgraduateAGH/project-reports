package pl.edu.agh.mwo.lazyminds;

import java.util.Scanner;

public class Reports {

	public static void main(String[] args) {

		System.out.println("Witamy w systemie raportowania czasu pracy");

		try (Scanner scanner = new Scanner(System.in)) {
			String input = "";
			String submenu = "";
			while (true) {

				// Menu g��wne u�ytkownika
				System.out.println("Podaj numer operacji lub wpisz 'exit' je�li chcesz zako�czy� program.");
				System.out.println("1\tRoczny raport godzin dla pracownik�w.");
				System.out.println("2\tRoczny raport godzin dla projekt�w.");
				System.out.println("3\tRaport projekt�w pracownika.");
				System.out.println("4\tRaport nak�adu pracy na projekt dla pracownik�w.");
				System.out.println("5\tRaport pracownik�w w danym projekcie");

				// odczyt opcji menu u�ytkownika
				input = scanner.nextLine();
				System.out.println(input);
				
				
				// rozpoznanie zako�czenia programu
				
				if(input.equals("exit")) {
					System.out.println("Program poprawnie zako�czy� prac�. Zapraszamy ponownie.");
					break;
				}
				

				int option;
				try {
					option = Integer.parseInt(input);
				} catch (NumberFormatException e) {
					System.out.println("Podaj liczb� z menu!");
					continue;
				}

				// obs�uga opcji menu
				switch (option) {
				case 1:
					System.out.println("Raport 1");
					System.out.println("Czy wyeksportowa� raport do PDF? Je�li tak, wpisz 't'.");
					submenu = scanner.nextLine();
					if (submenu.equals("t")) {
						System.out.println("Eksport raportu do PDF...");
					}
					break;
				case 2:
					System.out.println("Raport 2");
					break;
				case 3:
					System.out.println("Raport 3");
					break;
				case 4:
					System.out.println("Raport 4");
					break;
				case 5:
					System.out.println("Raport 5");
					break;				

				}
			}
		}

		
	}

}