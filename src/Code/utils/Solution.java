package Code.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author tungd
 */
public class Solution {

    // 3. An instructor cannot be assigned to teach different classes in the same time slot
    public static boolean passConstraint3(Data data, int[][] instructorTimeslot, int course, int instructor) {
        int time_slot_id = data.courses[course].getSlotId();
        return instructorTimeslot[instructor][time_slot_id] == 0;
    }

    // 4. Do not assign instructors to teach the subjects they have absolutely no interest in teaching
    public static boolean passConstraint4(Data data, int course, int instructor) {
        return data.E[instructor][course] != 0;
    }

    // 5. Do not assign an instructor to teach the subjects they cannot ensure the quantity
    public static boolean passConstraint5(Data data, int course, int instructor) {
        return data.F[instructor][course] != 0;
    }

    // 6. Do not assign an instructor to teach in time slots they cannot teach
    public static boolean passConstraint6(Data data, int course, int instructor) {
        return data.H[instructor][course] != 0;
    }

    // 7. Do not assign an instructor to teach fewer classes than the minimum number they are required to teach
    public static boolean passConstraint7(Data data, int[] instructorTotalCourse, int course, int instructor) {
        instructorTotalCourse[instructor] += 1;
        int total_course_need = 0;
        int total_assigned_course = 0;

        for (int i = 0; i < data.Ng; i++) {
            total_course_need += Integer.max(0, data.MinC[i] - instructorTotalCourse[i]);
            total_assigned_course += instructorTotalCourse[i];
        }

        int remain_course = data.Nc - total_assigned_course;
        instructorTotalCourse[instructor] -= 1;
        return total_course_need <= remain_course;
    }

    // 8. Do not assign an instructor to teach more classes than the maximum number they are allowed to teach
    public static boolean passConstraint8(Data data, int[] instructorTotalCourse, int course, int instructor) {
        return instructorTotalCourse[instructor] < data.MaxC[instructor];
    }

    public static boolean passAllConstraint(Data data, int[] instructorTotalCourse, int[][] instructorTimeslot, int course, int instructor) {
        return passConstraint3(data, instructorTimeslot, course, instructor)
                && passConstraint4(data, course, instructor)
                && passConstraint5(data, course, instructor)
                && passConstraint6(data, course, instructor)
                && passConstraint7(data, instructorTotalCourse, course, instructor)
                && passConstraint8(data, instructorTotalCourse, course, instructor);
    }

    public boolean passAllConstraint(Data data, int[] D) {
        int[] instructorTotalCourse = new int[data.Nc];
        int[][] instructorTimeslot = new int[data.Ng][data.Nt];

        for (int i = 0; i < data.Nc; i++) {
            int instructor = D[i];
            if (!passAllConstraint(data, instructorTotalCourse, instructorTimeslot, i, instructor)) {
                return false;
            }
            instructorTotalCourse[instructor] += 1;
            int timeslotId = data.courses[i].getSlotId();
            instructorTimeslot[instructor][timeslotId] = 1;
        }
        return true;
    }

    // Generate new solution
    public static int[] initSolution(Data data) {
        int[] gen = new int[data.Nc];
        int[] instructorTotalCourse = new int[data.Ng];
        int[][] instructorTimeslot = new int[data.Ng][data.Nt];
        int randomInstructor;

        for (int i = 0; i < data.Nc; i++) {
            DistributedRandomNumberGenerator rnd = new DistributedRandomNumberGenerator();
            for (int j = 0; j < data.Ng; j++) {
                if (passConstraint4(data, i, j) && passConstraint5(data, i, j)
                        && passConstraint6(data, i, j) && passConstraint3(data, instructorTimeslot, i, j)
                        && passConstraint8(data, instructorTotalCourse, i, j)) {
                    rnd.addNumber(j, 0.1);
                }
            }
            randomInstructor = rnd.getDistributedRandomNumber();
            gen[i] = randomInstructor;
            instructorTotalCourse[randomInstructor] += 1;
            int timeslotId = data.courses[i].getSlotId();
            instructorTimeslot[randomInstructor][timeslotId] = 1;
        }
        return gen;
    }

    public int[] levelSubjectPreference(Data data, int[] D) {
        int[] result = new int[data.Ng];
        for (int i = 0; i < data.Nc; i++) {
            result[D[i]] += data.E[D[i]][i];
        }
        return result;
    }

    public int[] levelTimeslotPreference(Data data, int[] D) {
        int[] result = new int[data.Ng];
        for (int i = 0; i < data.Nc; i++) {
            result[D[i]] += data.H[D[i]][i];
        }
        return result;
    }

    public static double calPayoffP0(Data data, int[] D) {
        double payoff_p0 = 0;
        for (int i = 0; i < data.Nc; i++) {
            payoff_p0 += data.F[D[i]][i];
        }
        return payoff_p0;
    }

    public static double[] calPayoffPi(Data data, int[] D) {
        double w1 = 1.0 / 3.0;
        double w2 = 1.0 / 3.0;
        double w3 = 1.0 / 3.0;
        int instructor;
        double[] payoff_pi = new double[data.Ng];
        int[] instructorTotalCourse = new int[data.Ng];

        for (int i = 0; i < data.Nc; i++) {
            instructor = D[i];
            payoff_pi[instructor] += w1 * (double) data.E[instructor][i] + w2 * (double) data.H[instructor][i];
            instructorTotalCourse[instructor] += 1;
        }

        for (int i = 0; i < data.Ng; i++) {
            payoff_pi[i] += w3 * (10 - Math.abs(data.K[i] - instructorTotalCourse[i]));
        }

        return payoff_pi;
    }

    public static double getFitness(Data data, int[] chromosome) {
        double w1 = 1.0 / 3.0;
        double w2 = 1.0 / 3.0;
        double w3 = 1.0 / 3.0;
        double w4 = 0.5;
        double w5 = 0.5;
        double fitness = 0;
        int[] instructorTotalCourse = new int[data.Ng];
        int[][] instructorTimeslot = new int[data.Ng][data.Nt];
        boolean pass_contraint = true;

        for (int i = 0; i < data.Nc; i++) {
            int instructor = chromosome[i];
            if (!passAllConstraint(data, instructorTotalCourse, instructorTimeslot, i, instructor)) {
                pass_contraint = false;
                break;
            }
            fitness += w4 * data.F[instructor][i] + w5 * (w1 * data.E[instructor][i] + w2 * data.H[instructor][i]);
            instructorTotalCourse[instructor] += 1;
            instructorTimeslot[instructor][data.courses[i].getSlotId()] = 1;
        }
        for (int i = 0; i < data.Ng; i++) {
            fitness += w5 * w3 * (10 - Math.abs(data.K[i] - instructorTotalCourse[i]));
        }
        if (!pass_contraint) {
            return fitness / 10000;
        }
        return fitness;
    }

    public static void writeSolutions(ArrayList<int[]> solutions, Data data, String algorithm, String sheetName) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("PayOff P0");
        for (int i = 1; i <= data.Ng; i++) {
            cell = row.createCell(i);
            cell.setCellValue("PayOff P" + i);
        }
        
        for (int i = 0; i < data.Nc; i++) {
            cell = row.createCell(27+i);
            cell.setCellValue("Course" + i);
        }
        cell = row.createCell(data.Nc + data.Ng + 2);
        cell.setCellValue("Fitness");

        int rowCount = 0;

        for (int[] s : solutions) {
            row = sheet.createRow(++rowCount);

            cell = row.createCell(0);
            cell.setCellValue(Solution.calPayoffP0(data, s));
            double[] payoff_pi = Solution.calPayoffPi(data, s);
            for (int i = 1; i <= data.Ng; i++) {
                cell = row.createCell(i);
                cell.setCellValue(payoff_pi[i - 1]);
            }
            
            for (int i = 0; i < data.Nc; i++) {
                cell = row.createCell(27 + i);
                cell.setCellValue(s[i]);
            }
            cell = row.createCell(data.Nc + data.Ng + 2);
            cell.setCellValue(Solution.getFitness(data, s));
        }
        try (FileOutputStream outputStream = new FileOutputStream("result//" + algorithm + "//" + sheetName + ".xlsx")) {
            workbook.write(outputStream);
            outputStream.close();
        }
    }
    
    public static void display(int[] solution){
        for (int i : solution) {
            System.out.print(i + ", ");
        }
        System.out.println("");
    }
}
