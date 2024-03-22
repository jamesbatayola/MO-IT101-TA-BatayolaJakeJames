import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Main {

    // Fields
    private static String months;
    private static int year;
    private static int employeeBasicSalary;
    private static String employeeHourlyRate;
    private static int employeeID;
    private static String employee[];

    private static int WEEKS_IN_MONTH = 4;
    private static int DAILY = 7;

    public static void main(String[] args) {

        start();
//        getAttendance("10025", 5);

    }

    public static void view() {
        System.out.println();
        System.out.println("============ Attendance Record ============");
        System.out.print("Enter month : ");

        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
            String userInput = inputReader.readLine();
            int month = Integer.parseInt(userInput);

            ArrayList<String> attendanceRecords = getAttendance(10025, month);
            if (attendanceRecords.isEmpty()) {
                System.out.println("No attendance records found for the specified month.");
                return;
            }

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        System.out.println("============= End of session =============");
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
    }

    public static ArrayList<String> getAttendance(int employeeID, int month) throws IOException, ParseException {
        String file = System.getProperty("user.dir") + "/src/source/Attendance.csv";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        ArrayList<String> attendanceRecord = new ArrayList<>();
        double totalSalary = 0.0;

        DecimalFormat df = new DecimalFormat("#.##"); // DecimalFormat for two decimal places

        while ((line = reader.readLine()) != null) {
            String[] employeeDataArray = line.split(",");
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            Date startDate = sdf.parse(employeeDataArray[1] + " " + employeeDataArray[2]);
            Date endDate = sdf.parse(employeeDataArray[1] + " " + employeeDataArray[3]);
            int recordMonth = startDate.getMonth() + 1; // Month is zero-based
            if (recordMonth == month && Integer.parseInt(employeeDataArray[0]) == employeeID) {
                long duration = endDate.getTime() - startDate.getTime();
                long hours = duration / (60 * 60 * 1000); // Milliseconds to hours
                double salary = hours * Double.parseDouble(employeeHourlyRate);
                totalSalary += salary;
                String formattedSalary = df.format(salary); // Formatting salary to two decimal places
                attendanceRecord.add(line + ", Hours Worked: " + hours + ", Salary: ₱" + formattedSalary);
                System.out.println(Arrays.toString(employeeDataArray) + ", Hours Worked: " + hours + ", Salary: ₱" + formattedSalary);
            }


        }

        String formattedTotalSalary = df.format(totalSalary); // Formatting total salary to two decimal places
        System.out.println("Total Salary for Month (no deduction) :: ₱" + formattedTotalSalary);

        System.out.println();

        double deducted1 = getPhilHealthCalculation(totalSalary);
        double deducted2 = getPagbigCalculation(deducted1);
        double deducted3 = getSSSCalculations(deducted2);

        System.out.println("Total Salary for Month w/ deduction :: ₱" + df.format(deducted3) );

        reader.close();
        start();
        return attendanceRecord;
    }

    // This method refreshed the field's data
    public static void refreshData() {
        employeeHourlyRate = "";
        employee = new String[0];
    }

    // This method fetch the employee details
    public static String getEmployeeDetails(String employeeID) {

        // locate the path of csv file
        String file = System.getProperty("user.dir") + "/src/source/Employee_Details.csv";

        BufferedReader reader = null;
        String line = "";
        String employeeSearched = "";

        try {
            reader = new BufferedReader(new FileReader(file));

            while ((line = reader.readLine()) != null) {
                // This will convert remove comma within each index
                String employeeData = line.replaceAll(",(?!(([^\"]*\"){2})*[^\"]*$)", ";x;");
                String[] employeeDataArray = employeeData.split(",");

                    if(employeeDataArray[0].equals(employeeID)){

                        employeeID = employeeDataArray[0];
                        employeeHourlyRate = employeeDataArray[18];

                        // Personal Information -------------
                        printPersonalInfo(employeeDataArray);

                        // Job Information -----------------
                        printJobInfo(employeeDataArray);

                        // ID'S -----------------------------
                        printID(employeeDataArray);

                        // Salaries ----------------------
                        System.out.println();
                        String monthlySalary = NumberFormat.getCurrencyInstance().format(Integer.parseInt(format(employeeDataArray[13])));
                        System.out.println("Base salary :: " + monthlySalary);
                        String hourlySalary =  employeeHourlyRate;
                        System.out.println("Hourly salary :: P" + hourlySalary);
                        break;
                    }

            }

            view();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return employeeSearched;
    }

    private static void printPersonalInfo(String[] employeeNumber) {
        System.out.println();
        System.out.println("-PERSONAL INFORMATION-");
        System.out.println("Employee ID :: " + employeeNumber[0]);
        System.out.println("Name :: " + employeeNumber[1] + " " + employeeNumber[2]);
        System.out.println("Birthdate :: " + employeeNumber[3]);
        System.out.println("Address :: " + format(employeeNumber[4]));
        System.out.println("Phone No. :: " + employeeNumber[5]);
    }

    private static void printJobInfo(String[] employeeNumber) {
        System.out.println();
        System.out.println("-JOB INFORMATION-");
        System.out.println("Status :: " + employeeNumber[10]);
        System.out.println("Position :: " + employeeNumber[11]);
        System.out.println("Immediate Supervisor :: " + format(employeeNumber[12]));
    }

    private static void printID(String[] employeeNumber) {
        System.out.println();
        System.out.println("-ID'S NUMBERS-");
        System.out.println("SSS :: " + employeeNumber[6]);
        System.out.println("Philhealth :: " + employeeNumber[7]);
        System.out.println("TIN :: " + employeeNumber[8]);
        System.out.println("Pag-ibig :: " + employeeNumber[9]);
    }

    private static String format(String data) {
        return data.replace(";x;", "").replace("\"", "");
    }

    public static void start() {
        refreshData();
        System.out.println();
        System.out.println("============= Start of Session =============");
        System.out.print("Enter employee number : ");

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

        try {
            String userInput = inputReader.readLine();
            String employeeDetail = getEmployeeDetails(userInput);

            if (!employeeDetail.isEmpty()) {
                view();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Method for getting PhilHealth contribution
    public static double getPhilHealthCalculation(double value) {
        double percent = 0.03;
        double deduction = value * percent;
        double result = value - deduction;
        return result;
    }

    // Method for getting Pag-big contribution
    public static double getPagbigCalculation(double value) {
        double percent = 0.03;
        double deduction = value * percent;
        double result = value - deduction;
        return result;
    }

    // Method for getting SSS contribution
    public static Double getSSSCalculations(double value) {
        if (value < 3250) {
            return value - 135.00;
        } else if (value >= 3250 && value < 3750) {
            return value - 157.50;
        } else if (value >= 3750 && value < 4250) {
            return value - 180.00;
        } else if (value >= 4250 && value < 4750) {
            return value - 202.50;
        } else if (value >= 4750 && value < 5250) {
            return value - 225.00;
        } else if (value >= 5250 && value < 5750) {
            return value - 247.50;
        } else if (value >= 5750 && value < 6250) {
            return value - 270.00;
        } else if (value >= 6250 && value < 6750) {
            return value - 292.50;
        } else if (value >= 6750 && value < 7250) {
            return value - 315.00;
        } else if (value >= 7250 && value < 7750) {
            return value - 337.50;
        } else if (value >= 7750 && value < 8250) {
            return value - 360.00;
        } else if (value >= 8250 && value < 8750) {
            return value - 382.50;
        } else if (value >= 8750 && value < 9250) {
            return value - 405.00;
        } else if (value >= 9250 && value < 9750) {
            return value - 427.50;
        } else if (value >= 9750 && value < 10250) {
            return value - 450.00;
        } else if (value >= 10250 && value < 10750) {
            return value - 472.50;
        } else if (value >= 10750 && value < 11250) {
            return value - 495.00;
        } else if (value >= 11250 && value < 11750) {
            return value - 517.50;
        } else if (value >= 11750 && value < 12250) {
            return value - 540.00;
        } else if (value >= 12250 && value < 12750) {
            return value - 562.50;
        } else if (value >= 12750 && value < 13250) {
            return value - 585.00;
        } else if (value >= 13250 && value < 13750) {
            return value - 607.50;
        } else if (value >= 13750 && value < 14250) {
            return value - 630.00;
        } else if (value >= 14250 && value < 14750) {
            return value - 652.50;
        } else if (value >= 14750 && value < 15250) {
            return value - 675.00;
        } else if (value >= 15250 && value < 15750) {
            return value - 697.50;
        } else if (value >= 15750 && value < 16250) {
            return value - 720.00;
        } else if (value >= 16250 && value < 16750) {
            return value - 742.50;
        } else if (value >= 16750 && value < 17250) {
            return value - 765.00;
        } else if (value >= 17250 && value < 17750) {
            return value - 787.50;
        } else if (value >= 17750 && value < 18250) {
            return value - 810.00;
        } else if (value >= 18250 && value < 18750) {
            return value - 832.50;
        } else if (value >= 18750 && value < 19250) {
            return value - 855.00;
        } else if (value >= 19250 && value < 19750) {
            return value - 877.50;
        } else if (value >= 19750 && value < 20250) {
            return value - 900.00;
        } else if (value >= 20250 && value < 20750) {
            return value - 922.50;
        } else if (value >= 20750 && value < 21250) {
            return value - 945.00;
        } else if (value >= 21250 && value < 21750) {
            return value - 967.50;
        } else if (value >= 21750 && value < 22250) {
            return value - 990.00;
        } else if (value >= 22250 && value < 22750) {
            return value - 1012.50;
        } else if (value >= 22750 && value < 23250) {
            return value - 1035.00;
        } else if (value >= 23250 && value < 23750) {
            return value - 1057.50;
        } else if (value >= 23750 && value < 24250) {
            return value - 1080.00;
        } else if (value >= 24250 && value < 24750) {
            return value - 1102.50;
        } else if (value >= 24750) {
            return 1125.00;
        }
        return null; // Value falls within no range
    }



}