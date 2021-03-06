package pl.edu.agh.mwo.lazyminds;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import pl.edu.agh.mwo.lazyminds.charts.SampleBarChart;
import pl.edu.agh.mwo.lazyminds.data.Reader;
import pl.edu.agh.mwo.lazyminds.model.User;
import pl.edu.agh.mwo.lazyminds.model.WorkUnit;
import pl.edu.agh.mwo.lazyminds.reports.*;

public class Reports {

	public static void main(String[] args) {

		// SETUP glownej kolekcji
		HashSet<User> allUsers = new HashSet<User>();

		// pobieranie wszystkich plikow
		Reader reader = new Reader();
		ArrayList<Path> allFiles = reader.getAllFiles(args[0]);
		for (Path path : allFiles) {
			// na potrzeby testowe
			// System.out.println("PLIK:\t"+path.toString());

			// ustalenie name i surname dla User
			Path newPath = path.getFileName();
			String fileName = newPath.toString().replace(".xls", "");
			String[] nameAndSurname = fileName.split("_");
			String name = nameAndSurname[0];
			String surname = nameAndSurname[1];
			// koniec ustawiania name i surname

			// current user
			User currentUser = null;
			// jesli ktorys user z allUsers ma takie imie i naziwko, to pobierz, jak nie to
			// stworz

			for (User existingUser : allUsers) {
				if (existingUser.getName().equals(name) && existingUser.getSurname().equals(surname)) {
					currentUser = existingUser;
					break;
				}
			}
			if (currentUser == null) {
				currentUser = new User(name, surname);
				allUsers.add(currentUser);
			}

			// dodanie userowi kolejnych work unitow
			ArrayList<WorkUnit> newWorkUnits = reader.readData(path);
			for (WorkUnit wu : newWorkUnits) {
				currentUser.addWorkUnit(wu);
			}
		}

		// TEST zczytywania
		/*
		 * for (User user: allUsers) {
		 * System.out.println("USER: "+user.getName()+" "+user.getSurname()); for
		 * (WorkUnit wk: user.getWorkUnits()) {
		 * System.out.println("\tPROJEKT: "+wk.getProject().getName()+", date: "+wk.
		 * getDate().toString()+", hours: "+wk.getHours()); } }
		 */

		/// start UI
		System.out.println("Witamy w systemie raportowania czasu pracy");

		try (Scanner scanner = new Scanner(System.in)) {
			String input = "";

			while (true) {

				main_menu();

				// odczytanie numeru opcji z menu
				input = scanner.nextLine();
				System.out.println(input);

				// rozpoznanie zakonczenia programu

				if (input.equals("exit")) {
					System.out.println("Program poprawnie zakończył prace. Zapraszamy ponownie.");
					break;
				}

				int option;
				try {
					option = Integer.parseInt(input);
				} catch (NumberFormatException e) {
					System.out.println("Podaj liczbę z menu!\n");
					continue;
				}

				int year;
				String name;
				String surname;
				String project;
				// obsluga wszystkich opcji menu
				switch (option) {
				case 1:
					System.out.println("RAPORT: Ilość godzin pracownika w roku.");
					ReportOneEmployeesWorkingHoursPerYear report1 = new ReportOneEmployeesWorkingHoursPerYear();

					year = askForYear(scanner);
					report1.generate(allUsers, year);

					exportReport(scanner);
					break;
				case 2:
					System.out.println("RAPORT: Ilość godzin w projekcie na rok.");
					ReportHowManyHoursPerProject report2 = new ReportHowManyHoursPerProject();

					year = askForYear(scanner);
					report2.generate(allUsers, year);

					exportReport(scanner);
					break;
				case 3:
					System.out.println("Raport 3");

					year = askForYear(scanner);
					System.out.println("Podaj imię pracownika:");
					name = askForWorker(scanner);
					System.out.println("Podaj nazwisko pracownika:");
					surname = askForWorker(scanner);
					System.out.println("Dane pracownika: " + name + " " + surname);

					exportReport(scanner);
					break;
				case 4:
					System.out.println("Raport 4");
					ReportsWorkingProcent report4 = new ReportsWorkingProcent();
					
					// year = askForYear(scanner);
					report4.generate(allUsers);
					exportReport(scanner);
					break;
				case 5:					
                    System.out.println("Raport 5");
                    project = askForProject(scanner);
                	exportReport(scanner);
                    break;
				case 6:
                    System.out.println("Raport 6: słupkowy");

                    SampleBarChart sampleBarChart= new SampleBarChart();
                    year = askForYear(scanner);
                    sampleBarChart.drawChart(allUsers, year);
                	
                    exportReport(scanner);
				}
			}

		}

	}

	private static boolean checkIfStringHasOnlyLetters(String s) {
		char[] chars = s.toCharArray();

		for (char c : chars) {
			if (!Character.isLetter(c)) {
				return false;
			}
		}
		return true;
	}
	
	private static String askForProject(Scanner scanner) {
		String project = "";
		
		System.out.println("Podaj nazwę projektu:");
		
		while(true) {
			project = scanner.nextLine();
			if (project.equals("")) {
				System.out.println("Błędna wartość. Podaj poprawną nazwę projektu!");
			} else {
				break;
			}
		}
		
		return project;
	}
	
	private static String askForWorker(Scanner scanner) {
		String worker = "";

		while (true) {
			worker = scanner.nextLine();
			if (!checkIfStringHasOnlyLetters(worker) || worker.equals("")) {
				System.out.println("Błędna wartość. Podaj poprawne dane!");
			} else {
				worker = worker.toLowerCase();
				break;
			}
		}

		return worker;
	}

	private static int askForYear(Scanner scanner) {
		System.out.println("Podaj rok:");
		int year = 0;
		while (year == 0) {
			if (scanner.hasNextInt()) {
				year = Integer.parseInt(scanner.nextLine());
			} else {
				System.out.println("Błędna wartość. Podaj poprawny rok!");
				scanner.nextLine();
			}
		}

		System.out.println("ROK WYBRANY: " + year + "\n");
		return year;
	}

	private static void exportReport(Scanner scanner) {
		String submenu;
		System.out.println("Czy wyeksportować raport do PDF? Jeśli tak, wpisz 't'.");
		submenu = scanner.nextLine();
		if (submenu.equals("t")) {
			System.out.println("Eksport raportu do PDF...\n");
		} else {
			System.out.println("Raport nie zostanie wyeksportowany. Powrót do menu głównego.\n");
		}
	}

	private static void main_menu() {
		// Menu główne użytkownika
		System.out.println("Podaj numer operacji lub wpisz 'exit' jeśli chcesz zakończyć program.");
		System.out.println("1\tRoczny raport godzin dla pracowników.");
		System.out.println("2\tRoczny raport godzin dla projektów.");
		System.out.println("3\tRaport projektów pracownika.");
		System.out.println("4\tRaport nakładu pracy na projekt dla pracowników.");
		System.out.println("5\tRaport pracowników w danym projekcie");
		System.out.println("6\tWykres słupkowy: roczny raport godzin dla projektów.");
	}

}
