import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

public class Main {

    // Fields
    public static String employeeHourlyRate;
    public static String employee[];

    public static int WEEKS_IN_MONTH = 4;
    public static int DAILY = 7;

    public static void main(String[] args) {

        defineDataType();

    }

    // This method refreshed the field's data
    public static void refreshData() {
        employeeHourlyRate = "";
        employee = new String[0];
    }

    // This method fetch the employee details
    public static String getEmployeeDetails(String employeeID) {

        // locate the path of csv file
        String file = System.getProperty("user.dir") + "/src/employee_details.csv";

        BufferedReader reader = null;
        String line = "";
        String employeeSearched = "";

        try {
            reader = new BufferedReader(new FileReader(file));

            while ((line = reader.readLine()) != null) {
                // This will convert remove comma within each index
                String employeeData = line.replaceAll(",(?!(([^\"]*\"){2})*[^\"]*$)", ";x;");
                String[] employeeDataArray = employeeData.split(",");

                if((Integer.parseInt(employeeDataArray[0]) > 34) || Integer.parseInt(employeeDataArray[0]) < 0) {
                    System.out.println("Employee " + employeeID + " not found");
                }

                if(employeeDataArray[0].equals(employeeID)){

                    // Personal Information ---------------------------

                    System.out.println();
                    System.out.println("-PERSONAL INFORMATION-");
                    System.out.println("Employee ID :: " + employeeDataArray[0]);
                    System.out.println("Name :: " + employeeDataArray[1] + " " + employeeDataArray[2]);
                    System.out.println("Birthdate :: " + employeeDataArray[3]);
                    System.out.println("Address :: " + format(employeeDataArray[4]));
                    System.out.println("Phone No. :: " + employeeDataArray[5]);
                    System.out.println();

                    // Calculations -------------------------------

                    String monthlySalary = NumberFormat.getCurrencyInstance().format(Integer.parseInt(employeeDataArray[13]));
                    System.out.println("Monthly salary :: " + monthlySalary);

                    double doubledMonthly = Double.parseDouble(format(employeeDataArray[13]));

                    int weeklySalary = (int) doubledMonthly / WEEKS_IN_MONTH;
                    System.out.println("Weekly salary :: " + weeklySalary);

                    int dailySalary = (int) doubledMonthly / DAILY;
                    System.out.println("Daily salary :: " + dailySalary);

                    // Contributions -------------------------------

//                    int philHealth = (int) monthlySalary / 0.3;
                }

            }

            System.out.println("======= End of session =======");
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            defineDataType();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return employeeSearched;
    }

    public static String format(String data) {
        return data.replace(";x;", "").replace("\"", "");
    }

    public static void defineDataType() {
        refreshData();
        System.out.println();
        System.out.println("======= Start of Session =======");
        System.out.print("Enter employee number : ");

        var inputReader = new BufferedReader(new InputStreamReader(System.in));

        try {

            String userInput = inputReader.readLine();
            String employeeDetail = getEmployeeDetails(userInput);

            if(!employeeDetail.equals("")) {
                String[] row = employeeDetail.split(", ");
                employee = row;
                employeeHourlyRate = row[18];
                System.out.println(employee[Integer.parseInt(userInput)]);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String getSSSCalculation() {
        String monthlySalary = employee[13].replace(";x;", "").replace("\"", "");
        System.out.println(monthlySalary);
        return null;
    }

    public static void Print(String data) {

    }

}