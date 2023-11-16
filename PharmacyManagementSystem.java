import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

class Medicine {
    String name;
    double price;
    int quantity;
    String manufacturingDate;
    String expiryDate;
    String dosage;
    int slno;
    String type;

    public Medicine(String name, double price, int quantity, String dosage, String type) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.dosage = dosage;
        generateManufacturingAndExpiryDates();
        this.slno = 0;
        this.type = type;
    }

    private void generateManufacturingAndExpiryDates() {
        Random random = new Random();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -random.nextInt(5));
        calendar.add(Calendar.MONTH, -random.nextInt(12));

        Date expiry = calendar.getTime();
        calendar.setTime(expiry);
        calendar.add(Calendar.YEAR, 3);

        this.expiryDate = formatDate(calendar.getTime());
        this.manufacturingDate = formatDate(expiry);
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
        return dateFormat.format(date);
    }
}



public class PharmacyManagementSystem extends JFrame implements ActionListener {
    private ArrayList<Medicine> medicines;
    private JTextArea outputArea;
    private String customerName;
    private String phoneNumber;
    private int slnoCounter;
    private String customerAge;

    public PharmacyManagementSystem() {
        setTitle("Pharmacy Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        medicines = new ArrayList<>();
        slnoCounter = 1;

        JButton customerDetailsButton = new JButton("Customer Details");
        JButton addButton = new JButton("Add Medicine");
        JButton updateButton = new JButton("Update Quantity");
        JButton deleteButton = new JButton("Delete Medicine");
        JButton calculateButton = new JButton("Calculate Bill");
        JButton displayButton = new JButton("Display Bill");

        outputArea = new JTextArea(20, 50);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        customerDetailsButton.addActionListener(this);
        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        calculateButton.addActionListener(this);
        displayButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1));
        buttonPanel.add(customerDetailsButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(displayButton);

        add(buttonPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Customer Details")) {
            enterCustomerDetails();
        } else if (e.getActionCommand().equals("Add Medicine")) {
            addMedicine();
        } else if (e.getActionCommand().equals("Update Quantity")) {
            updateQuantity();
        } else if (e.getActionCommand().equals("Delete Medicine")) {
            deleteMedicine();
        } else if (e.getActionCommand().equals("Calculate Bill")) {
            calculateBill();
        } else if (e.getActionCommand().equals("Display Bill")) {
            displayBill();
        }
    }

    private void enterCustomerDetails() {
        customerName = JOptionPane.showInputDialog("Enter customer name:");
        phoneNumber = JOptionPane.showInputDialog("Enter phone number:");
        customerAge = JOptionPane.showInputDialog("Enter customer age:");
        outputArea.setText("");
        outputArea.append("Customer Details have been successfully entered\n");
        outputArea.append("Customer name: " + customerName + "\nPhone number: " + phoneNumber + "\nCustomer age: " + customerAge + "\n\n");
    }

    private void addMedicine() {
        String name = JOptionPane.showInputDialog("Enter medicine name:");
        double price = Double.parseDouble(JOptionPane.showInputDialog("Enter price:"));
        int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity:"));

        String[] medOptions;
        String[] dosageOptions;
        if (Integer.parseInt(customerAge) <= 10) {
            medOptions = new String[]{"Syrup"};
            //dosageOptions = new String[]{"NA"};
             if (name.equalsIgnoreCase("dolo")) {
                dosageOptions = new String[]{"100ml", "120ml","125ml"};
            } else if (name.equalsIgnoreCase("aspirin")) {
                dosageOptions = new String[]{"10-15ml", "20-25ml"};
            } else if (name.equalsIgnoreCase("avomine")) {
                dosageOptions = new String[]{"NA"};
            } else {
                dosageOptions = new String[]{"NA"};
            }
        } else {
            medOptions = new String[]{"Tablet"};
            if (name.equalsIgnoreCase("dolo")) {
                dosageOptions = new String[]{"500mg", "650mg"};
            } else if (name.equalsIgnoreCase("aspirin")) {
                dosageOptions = new String[]{"81mg", "325mg", "500mg"};
            } else if (name.equalsIgnoreCase("avomine")) {
                dosageOptions = new String[]{"25mg"};
            } else {
                dosageOptions = new String[]{"10mg", "50mg", "60mg"};
            }
        }


        String selectedMed = (String) JOptionPane.showInputDialog(
            null,
            "Select medtype:",
            "Medicine type",
            JOptionPane.PLAIN_MESSAGE,
            null,
            medOptions,
            medOptions[0]
        );

        String selectedDosage = (String) JOptionPane.showInputDialog(
            null,
            "Select dosage:",
            "Dosage Selection",
            JOptionPane.PLAIN_MESSAGE,
            null,
            dosageOptions,
            dosageOptions[0]
        );

        

        Medicine medicine = new Medicine(name, price, quantity,selectedDosage,selectedMed);
        medicine.slno = slnoCounter;
        slnoCounter++;
        medicines.add(medicine);

        outputArea.append("Medicine added: " + medicine.name + "\n\n");
    }

    private void updateQuantity() {
        String name = JOptionPane.showInputDialog("Enter medicine name:");
        int quantityChange = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity change:"));

        for (Medicine medicine : medicines) {
            if (medicine.name.equalsIgnoreCase(name)) {
                medicine.quantity += quantityChange;
                outputArea.append("Quantity updated for " + medicine.name + ": " + medicine.quantity + "\n\n");
                return;
            }
        }

        outputArea.append("Medicine not found.\n");
    }

    private void deleteMedicine() {
        String name = JOptionPane.showInputDialog("Enter medicine name:");

        for (Medicine medicine : medicines) {
            if (medicine.name.equalsIgnoreCase(name)) {
                medicines.remove(medicine);
                outputArea.append("Medicine deleted: " + medicine.name + "\n\n");
                return;
            }
        }

      

        outputArea.append("Medicine not found.\n");
    }

    private void calculateBill() {
        double totalBill = calculateTotalBill();

        outputArea.append("Total Bill: " + totalBill + "\n");
    }


    

    private void displayBill() {
        double totalBill = calculateTotalBill();

        outputArea.setText("");
        //outputArea.setFont(new Font(Font.MONOSPACED,Font.PLAIN,12));
        
        outputArea.append("\t\t\tWELCOME TO VITALSFIT\t\t\n");
        outputArea.append("\t\t\t123, Fitness Street,\n\t\t\tHealthVille, Bangalore-560035\t\t\n");
        outputArea.append("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        outputArea.append("Name: " + customerName + "\n");
        outputArea.append("Number: " + phoneNumber + "\n\n");
        outputArea.append("Age: " + customerAge + "\n\n");
        outputArea.append("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        outputArea.append("Sl.No\tMedicine\tType\tDosage\tManufacturing Date\tExpiry Date\tQuantity\tPrice\n");
        outputArea.append("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        for (Medicine medicine : medicines) {
            outputArea.append(medicine.slno + "\t" + medicine.name + "\t" + medicine.type + "\t" + medicine.dosage + "\t"
                    + medicine.manufacturingDate + "\t\t" + medicine.expiryDate + "\t" + medicine.quantity + "\t" + medicine.price + "\t\n");
        }

        outputArea.append("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        outputArea.append("\nTotal Bill: " + totalBill + "\n");
        outputArea.append("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        outputArea.append("\t\tThank You for shopping, Do visit again!\t\t\n");
       
    }

    private double calculateTotalBill() {
        double totalBill = 0;

        for (Medicine medicine : medicines) {
            totalBill += medicine.price * medicine.quantity;
        }

        return totalBill;
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> new PharmacyManagementSystem());
    
    }
}