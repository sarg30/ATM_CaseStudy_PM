import java.util.Scanner;
import java.util.Random;
import java.util.jar.Attributes.Name;

import javax.sound.sampled.BooleanControl;

interface ATM_Setup{
    //display and validate login
    void display();
    Boolean checkDetails();
}

//class where admin can make changes to the atm system
class admin extends UserData implements ATM_Setup{
    private int ID;
    private int password;
    private double dailyLimit;
    private double depositLimit;
    
    //constructor of admin class
    admin(){
        this.ID=999999;
        this.password=999999;
        this.dailyLimit=20000;
        this.depositLimit=500000;
    }

    public void display(){
        //a list of all the functions the admin can perform and
        System.out.println("Enter the number corresponding to the operation you would like to perform");
        System.out.println("1. Get the daily deposit limit");
        System.out.println("2. Set the daily deposit limit");
        System.out.println("3. Get the daily withdrawal limit");
        System.out.println("4. Set the daily withdrawal limit");
        System.out.println("5. Display user data");
        System.out.println("6. Turn off ATM machine");
        System.out.println("7. Log Out");
    }
    
    //
    public double getDailyLimit() {
        //
        return dailyLimit;
    }
    
    //method to set the daily withdrawal limit
    public void setDailyLimit() {
        System.out.println("Enter the daily withdrawal limit:-");
        int lim=ATM.in.nextInt();
        dailyLimit=lim;
        System.out.println("The daily withdrawal limit has been set to "+lim+" rupees");
    }
    
    //to display the daily deposit limit to the users
    public double getDepositLimit() {
        // System.out.print("The daily deposit limit is(rupees) "+depositLimit);
        return depositLimit;
    }

    //to set the daily deposit limit for the users
    public void setDepositLimit() {
        System.out.println("Enter the daily deposit limit:-");
        int lim=ATM.in.nextInt();
        depositLimit=lim;
        System.out.println("The daily deposit limit has been set to "+lim+" rupees");
    }

    //to check if the login details of admin are correct
    public Boolean checkDetails(){
        int id,pass;
        System.out.println("Please enter your ID");
        id=ATM.in.nextInt();
        System.out.println("Please enter your password");
        pass=ATM.in.nextInt();
        return (id==ID && pass==password);
    }

    //get the user data(excluding passwords)
    public void getUserData() {
        System.out.println("Please enter the id of the user whose details you want");
        int id=ATM.in.nextInt();
        System.out.println("The customer ID is "+id);
        System.out.println("The customer USERNAME is "+getName(id));
        System.out.println("The customer PHONENUMBER is "+getPhoneNo(id));
        System.out.println("The customer BALANCE is "+getBalance(id));
    }
    
}

//for conversion from one currency to other
class Conversion{
    private double conversionRate[] =new double[]{1,75.04,87.04,103.14};
    //0.INR
    //1.USD
    //2.EUR
    //3.GBP

    //returns the conversion rate corresponding to the currency of deposit
    public double getConversionRate(int idx){
        return conversionRate[idx];
    }
}

//contains all the funcionalies corresponding to the actual transaction process
class Transaction extends UserData implements ATM_Setup{

    private int id;     //thr id of the current user

    Conversion conv=new Conversion();
    admin admobj=new admin();

    public void display(){
        //displaying all the functionalities for the user
        System.out.println("Enter the number corresponding to the operation you would like to perform");
        System.out.println("1. Deposit Funds");
        System.out.println("2. Withdraw funds");
        System.out.println("3. Transfer funds to some other account");
        System.out.println("4. Check Balance");
        System.out.println("5. Change PhoneNumber");
        System.out.println("6. Change UserName");
        System.out.println("7. Change Password");
        System.out.println("8. Check withdrwal and deposit limits(per day)");
        System.out.println("9. Log Out");
    }

    //chose between user and admin
    public void userSelect(){
        System.out.println("Welcome to Bank of Liberty!");
        System.out.println("Please enter the number corresponding to your use type:-");
        System.out.println("1. Admin");
        System.out.println("2. Customer");
    }

    //to check if the login details of customer are correct
    public Boolean checkDetails(){
        int id,pass;
        System.out.println("Please enter your ID");
        id=ATM.in.nextInt();
        System.out.println("Please enter your password");
        pass=ATM.in.nextInt();
        return (id==pass);
    }

    //facilitates the deposit process
    public void deposit(){
        System.out.println("Enter the currency you are going to deposit in:-");
        System.out.println("0. INR\n1. USD\n2. EUR\n3. GBP");
        int convRate=ATM.in.nextInt();
        System.out.println("Enter the mode of deposit:-");
        System.out.println("0. Cash\n1. Cheque\n2. Coins");
        int mode=ATM.in.nextInt();
        System.out.println("Enter the amount you want to deposit:-");
        double amount=ATM.in.nextInt();
        amount=amount*conv.getConversionRate(convRate);
        super.updateBalance(this.id, amount);
    }

    //facilitates the withdrawal process
    public void withdraw(){
        System.out.println("Enter the amount you want to withdraw:-");
        double amount=ATM.in.nextInt();
        super.updateBalance(this.id, -amount);
    }

    //facilitates the transfer process
    public void transfer(){
        System.out.println("Enter the account number of the account you want to transfer to");
        int id2=ATM.in.nextInt();
        if(id2>99999 || id2<10000){
            System.out.println("The entered Account NUmber is invalid");
        }
        else{
            System.out.println("Enter the amount you want to transfer");
            double amount=ATM.in.nextDouble();
            if(amount<=getBalance(id) || amount<=admobj.getDailyLimit() || amount<=admobj.getDepositLimit()){
                System.out.println("The given amount cannot be transferred!Please check your balance and daily transaction limits.");
            }
            else{
                System.out.println("Transaction Successfull!");
                updateBalance(id2, amount);
                updateBalance(id, -amount);
            }
        }
    }

    //send otp and checks whether the entered otp is correct or not
    public Boolean OTP(){
        Random rand = new Random();
		int otp = Math.abs(rand.nextInt())%10000+10000;
        System.out.println("A 5 digit OTP has been sent on your registered number");
        System.out.println("Your OTP for logging in to Bank of Liberty account is "+otp);
        System.out.println("Please enter below the 5-digit OTP you recieved");
        int rec=ATM.in.nextInt();
        return rec==otp;
    }

    //getter function for the id of the current user
    public int getId(){
        return id;
    }

    //log out of account
    public void exit(){
        System.out.println("Thank you for being a part of Bank of Liberty!");
        System.out.println("Have a nice day!");
    }

}

//facilitates all the queries regarding the data of the customer
class UserData{
    private String[] Name=new String[100000];
    private int[] AccountNo=new int[100000];
    private int[] AccPassword=new int[100000];
    private double[] Balance=new double[100000];
    private String[] PhoneNo=new String[100000];

    //constructor for the class UserData
    public UserData(){
        setAccountDetails();
    }

    //to set data of new account which can be later changes by the user
    void setAccountDetails(){
        for(int i=0;i<100000;i++){
            AccountNo[i]=i;
            AccPassword[i]=i;
            Balance[i]=0;
            PhoneNo[i]="";
            Name[i]="";
        }
    }

    //getter function for the balance of the current user
    public double getBalance(int id){
        return Balance[id];
    }

    //getter function for the phone number of the current user
    public String getPhoneNo(int id){
        return PhoneNo[id];
    }

    //getter function for the user name of the current user
    public String getName(int id){
        return Name[id];
    }

    //to facilitate changing of user name
    public void changeName(int id){
        System.out.println("Please enter the new username that you would like to change to:- ");
        String name=ATM.in.next();
        Name[id]=name;
        System.out.println("Your new username has been set to "+Name[id]);
    }

    //to facilitate changing of phone number
    public void changePhone(int id){
        System.out.println("Please enter the new PHONENUMBER that you would like to change to:- ");
        String num=ATM.in.next();
        PhoneNo[id]=num;
        System.out.println("Your PHONENUMBER has been set to "+PhoneNo[id]);
    }

    //to facilitate changing of password
    public void changePin(int id){
        System.out.println("Please enter the new PIN that you would like to change to:- ");
        int pin=ATM.in.nextInt();
        AccPassword[id]=pin;
        System.out.println("Your PIN has been successfully updated!");
    }

    //to update the balance after operation such as deposit,withdrawa and transfer
    public void updateBalance(int id,double amount){
        if(amount>0){
            if(amount>20000){
                System.out.println("You cannot deposit more than the daily Deposit limit!");
            }
            else{
                Balance[id]+=amount;
                System.out.println("Transaction successful!Your new balance is:- "+Balance[id]);
            }
        }
        else{
            if(-amount>getBalance(id)){
                System.out.println("You dont have enough funds in your account!");
            }
            else if(-amount>20000){
                System.out.println("The amount is more than the daly limit,TRANSACTION UNSECCESSFUL!");
            }
            else{
                Balance[id]+=amount;
                System.out.println("Transaction successful!Your new balance is:- "+Balance[id]);
            }
        }
    }
}

public class ATM {
    static Scanner in=new Scanner(System.in);
    public static void main(String[] args) {
        admin adm=new admin();
        Transaction tran=new Transaction();

        tran.userSelect();
        int user=in.nextInt();
        Boolean loggedInUser=false;
        Boolean loggedInAdmin=false;
        Boolean sendotp=true;
        Boolean turnoff=false;
        while(user!=1 && user!=2){
            System.out.println("Please enter valid option number");
            tran.userSelect();
            user=in.nextInt();
        }
        while(true){
            if(turnoff){
                System.out.println("Switching off system");
                break;
            }
            if(user==1){
                Boolean flag=true;
                if(!loggedInAdmin){
                    flag=adm.checkDetails();
                    if(flag){
                        flag=tran.OTP();
                    }
                    loggedInAdmin=flag;
                }
                if(!flag){
                    System.out.println("The entered password or OTP are incorrect");
                    user=0;
                    continue;
                }
                sendotp=false;
                adm.display();
                int admfunc=in.nextInt();
                switch(admfunc){
                    case 1:
                        System.out.println("The daily deposit limit is(rupees):- "+adm.getDailyLimit());
                        break;
                    case 2:
                        adm.setDailyLimit();
                        break;
                    case 3:
                        System.out.println("The daily withdrawl limit is(rupees):- "+adm.getDepositLimit());
                        break;
                    case 4:
                        adm.setDepositLimit();
                        break;
                    case 5:
                        adm.getUserData();
                        break;
                    case 6:
                        turnoff=true;
                        break;
                    case 7:
                        tran.exit();
                        tran.userSelect();
                        user=in.nextInt();
                        loggedInAdmin=false;
                        sendotp=true;
                        break;
                    default:
                        System.out.println("Please enter valid option number");
                }
            }
            else if(user==2){
                Boolean flag=true;
                if(!loggedInUser){
                    flag=tran.checkDetails();
                    if(flag){
                        flag=tran.OTP();
                    }
                    loggedInUser=flag;
                }
                if(!flag){
                    System.out.println("The entered password or OTP are incorrect");
                    user=0;
                    continue;
                }
                //imlement the otp thing
                sendotp=false;
                tran.display();
                int userfnc=in.nextInt();
                switch(userfnc){
                    case 1:
                        tran.deposit();
                        break;
                    case 2:
                        tran.withdraw();
                        break;
                    case 3:
                        //complete this
                        tran.transfer();
                        break;
                    case 4:
                        System.out.println("Your current balance is(rupees) "+tran.getBalance(tran.getId()));
                        break;
                    case 5:
                        tran.changePhone(tran.getId());
                        break;
                    case 6:
                        tran.changeName(tran.getId());
                        break;
                    case 7:
                        tran.changePin(tran.getId());
                        break;
                    case 8:
                        System.out.println("The daily deposit limit is(rupees):- "+adm.getDepositLimit());
                        System.out.println("The daily withdrawl limit is(rupees):- "+adm.getDailyLimit());
                        break;
                    case 9:
                        tran.exit();
                        loggedInUser=false;
                        user=0;
                        sendotp=true;
                        break;
                    default:
                        System.out.println("Please enter valid option number");
                }
            }
            else{
                while(user!=1 && user!=2){
                    tran.userSelect();
                    user=in.nextInt();
                    if(user!=1 && user!=2) System.out.println("Please enter valid option number");
                }
            }
        }
    }
}
