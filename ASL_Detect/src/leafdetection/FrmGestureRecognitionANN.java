/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leafdetection;

import algoANN.NeuralNetwork;
import com.memetix.mst.MicrosoftTranslatorAPI;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class FrmGestureRecognitionANN extends javax.swing.JFrame {

    JLabel hand1Data[][], hand2Data[][];
    boolean isSet = false;
    Timer t;
    boolean running;
    MyTimerTask task;
    int progress;
    int compareCount, compareMax = 5;
    int prevMin;
    double setData[][] = new double[2][18];
    Vector<Double> scoresxyz, scoresxz;
    MainForm parent;
    private static final String VOICENAME = "kevin16";
    //Voice voice;
    // VoiceManager voiceManager;
    String output = "";
    BufferedImage imgin;
    Vector<String> charVal_ISL, charVal_ASL;
    String lang[] = {"ENGLISH","FRENCH", "SPANISH", "PORTUGUESE", "ITALIAN", "DUTCH", "GERMAN", "HINDI"};
    Language langArray[];
    String clientId = "MultiLanguageTrainer";
    String clientSecret = "XECz/+NIIRHQUkD93H7yV1o9ezLYhVnih9l1Jbua6WM=";
    String finalString = "";

    public FrmGestureRecognitionANN(MainForm parent) {
        this.parent = parent;
        initComponents();
        Dimension sd = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(sd.width / 2 - this.getWidth() / 2, sd.height / 2 - this.getHeight() / 2);
        for (int i = 0; i < lang.length; i++) {
            jComboBox1.addItem(lang[i]);
        }
        int cnt = 100;
        for (int i = 0; i < 50; i++) {
            jComboBox2.addItem(cnt);
            cnt += 100;
        }
        jComboBox2.setSelectedIndex(10);

        langArray = new Language[8];
        langArray[7] = Language.ENGLISH;
        langArray[0] = Language.CHINESE_SIMPLIFIED;
        langArray[1] = Language.SPANISH;
        langArray[2] = Language.PORTUGUESE;
        langArray[3] = Language.ITALIAN;
        langArray[4] = Language.DUTCH;
        langArray[5] = Language.GERMAN;
        langArray[6] = Language.HINDI;
        MicrosoftTranslatorAPI.setClientId(clientId);
        MicrosoftTranslatorAPI.setClientSecret(clientSecret);
        charVal_ISL = new Vector<>();
        charVal_ASL = new Vector<>();

        try {
            imgin = ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\imgPack\\back.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        jPanel3.setBackground(new Color(0, 0, 0, 80));

        scoresxyz = new Vector<Double>();
        scoresxz = new Vector<Double>();

        hand1Data = new JLabel[6][3];
        hand2Data = new JLabel[6][3];
//        readTrainLeft();
//        readTrainRight();
//        readTrainBoth();
        charVal_ISL.add("3");
        charVal_ISL.add("4");
        charVal_ISL.add("5");
        charVal_ISL.add("6");
        charVal_ISL.add("7");
        charVal_ISL.add("8");
        charVal_ISL.add("9");
        charVal_ISL.add("A");
        charVal_ISL.add("B");
        charVal_ISL.add("C");
        charVal_ISL.add("D");
        charVal_ISL.add("E");
        charVal_ISL.add("F");
        charVal_ISL.add("G");
        charVal_ISL.add("H");
        charVal_ISL.add("I");
        charVal_ISL.add("K");
        charVal_ISL.add("L");
        charVal_ISL.add("M");
        charVal_ISL.add("N");
        charVal_ISL.add("O");
        charVal_ISL.add("P");
        charVal_ISL.add("Q");
        charVal_ISL.add("R");
        charVal_ISL.add("S");
        charVal_ISL.add("T");
        charVal_ISL.add("U");
        charVal_ISL.add("V");
        charVal_ISL.add("W");
        charVal_ISL.add("X");
        charVal_ISL.add("Y");
        charVal_ISL.add("Z");




        charVal_ASL.add("1");
        charVal_ASL.add("3");
        charVal_ASL.add("4");
        charVal_ASL.add("5");
        charVal_ASL.add("7");
        charVal_ASL.add("8");
        charVal_ASL.add("9");
        charVal_ASL.add("10");
        charVal_ASL.add("A");
        charVal_ASL.add("B");
        charVal_ASL.add("C");
        charVal_ASL.add("D");
        charVal_ASL.add("E");
        charVal_ASL.add("F");
        charVal_ASL.add("G");
        charVal_ASL.add("H");
        charVal_ASL.add("I");
        charVal_ASL.add("K");
        charVal_ASL.add("L");
        charVal_ASL.add("M");
        charVal_ASL.add("N");
        charVal_ASL.add("O");
        charVal_ASL.add("P");
        charVal_ASL.add("Q");
        charVal_ASL.add("R");
        charVal_ASL.add("S");
        charVal_ASL.add("T");
        charVal_ASL.add("U");
        charVal_ASL.add("V");
        charVal_ASL.add("W");
        charVal_ASL.add("X");
        charVal_ASL.add("Y");



        initArray();
        task = new MyTimerTask();
        t = new Timer();
        t.schedule(task, 1, 1);
    }

    class MyTimerTask extends TimerTask {

        double temp10, temp20, temp11, temp21, d1, d2, d3;

        public MyTimerTask() {
        }

        @Override
        public void run() {
            // Have the sample listener receive events from the controller
            // controller.addListener(listener);

            try {
                Thread.sleep(Long.parseLong(jComboBox2.getSelectedItem().toString()));
            } catch (Exception e) {
                ;
            }

            int index = 0;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 3; j++) {
                    if (j != 1) {//left hand
                        GlobalData.data[0][index] = (GlobalData.data[0][index] < -300.0) ? -300.0 : GlobalData.data[0][index];
                        GlobalData.data[0][index] = (GlobalData.data[0][index] > 300.0) ? 300.0 : GlobalData.data[0][index];
                        GlobalData.data[0][index] = GlobalData.data[0][index] + 300.0;
//right hand
                        GlobalData.data[1][index] = (GlobalData.data[1][index] < -300.0) ? -300.0 : GlobalData.data[1][index];
                        GlobalData.data[1][index] = (GlobalData.data[1][index] > 300.0) ? 300.0 : GlobalData.data[1][index];
                        GlobalData.data[1][index] = GlobalData.data[1][index] + 300.0;
                    }
                    index++;
                }
                if (i != 0) {
                    GlobalData.data[0][index - 3] = GlobalData.data[0][0] - GlobalData.data[0][index - 3];
                    GlobalData.data[0][index - 2] = GlobalData.data[0][1] - GlobalData.data[0][index - 2];
                    GlobalData.data[0][index - 1] = GlobalData.data[0][2] - GlobalData.data[0][index - 1];

                    GlobalData.data[1][index - 3] = GlobalData.data[1][0] - GlobalData.data[1][index - 3];
                    GlobalData.data[1][index - 2] = GlobalData.data[1][1] - GlobalData.data[1][index - 2];
                    GlobalData.data[1][index - 1] = GlobalData.data[1][2] - GlobalData.data[1][index - 1];
                }
            }
//label value in hand1data
            index = 0;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 3; j++) {
                    setData[0][index] = GlobalData.data[0][index];
                    setData[1][index] = GlobalData.data[1][index];
                    hand1Data[i][j].setText("" + (int) setData[0][index]);
                    hand2Data[i][j].setText("" + (int) setData[1][index]);
                    index++;
                }
            }

            if (jRadioASL.isSelected()) {
                if (GlobalData.currHand == 0) {
                    double inputFeatures[] = getFeatures(setData[1]);
//                for (int i = 0; i < inputFeatures.length; i++) {
//                    System.out.print("," + decimal(inputFeatures[i]));
//                }

                    try {
                        ObjectInputStream oIn = new ObjectInputStream(new FileInputStream(System.getProperty("user.dir") + "\\train.dat"));
                        NeuralNetwork nn = (NeuralNetwork) (oIn.readObject());
                        nn.setInputs(inputFeatures);
                        double curr_out_D[] = nn.runNetwork();
                        int data[] = convertToInt(curr_out_D);
                        //  System.out.println();
                        String output = "";
                        boolean found = false;
                        for (int ii = 0; ii < curr_out_D.length; ii++) {
                            if (data[ii] == 1) {
                                output += charVal_ASL.get(ii) + "";
                                found = true;
                            }
                        }
                        if (found) {
                            //   System.out.println("  " + output);
                            jTextField1.setText(output);
                            finalString += jTextField1.getText();
                            jTextField2.setText(finalString);

                        }

                    } catch (Exception e) {
                    }

                }

            
            }


        }
    }

    double[] getFeatures(double data[]) {
        double finalRow[] = new double[30];
        Vector<SingleDistanceFeature> dataVal = new Vector<>();
        for (int i = 0; i < 18; i = i + 3) {
            SingleDistanceFeature sr = new SingleDistanceFeature();
            for (int j = 0; j < 3; j++) {
                sr.d[j] = data[i + j];
            }
            // sr.print();
            dataVal.add(sr);
        }
        int index = 0;
        for (int i = 1; i < dataVal.size(); i++) {
            finalRow[index] = (getDistance(dataVal.get(0).d, dataVal.get(i).d) / 600);
            finalRow[index + 15] = (thetaAngle(dataVal.get(0).d, dataVal.get(i).d) / 150);
            index++;
            //   System.out.println("INDEX:   "+index );
        }

        //  System.out.println("GOT CURRENT INDEX:   "+index );

        for (int i = 1; i < dataVal.size(); i++) {
            for (int j = i + 1; j < dataVal.size(); j++) {
                finalRow[index] = (getDistance(dataVal.get(i).d, dataVal.get(j).d) / 600);
                finalRow[index + 15] = (thetaAngle(dataVal.get(i).d, dataVal.get(j).d) / 150);
                index++;


            }
        }

//        System.out.println(" INDEX:   " + index + "  \n");
//
//        for (int i = 0; i < finalRow.length; i++) {
//            System.out.println("  " + finalRow[i]);
//        }
        return finalRow;
    }

    double decimal(double d) {
        int val = (int) (d * 100);
        double dVal = val * 1.0 / 100;
        return dVal;

    }

    public int[] convertToInt(double outD[]) {
        int outI[] = new int[outD.length];
        System.out.println();
        for (int i = 0; i < outD.length; i++) {
            outI[i] = 0;
        }

        int maxIndex = 0;
        double maxDist = 0;
        for (int i = 0; i < outD.length; i++) {
            if (maxDist < outD[i]) {
                maxDist = outD[i];
                maxIndex = i;
            }
        }
        outI[maxIndex] = 1;
        return outI;
    }

    double[] getFeaturesRightHand(double data[]) {
        double finalRow[] = new double[30];
        Vector<SingleDistanceFeature> dataVal = new Vector<>();
        for (int i = 0; i < 18; i = i + 3) {
            SingleDistanceFeature sr = new SingleDistanceFeature();
            for (int j = 0; j < 3; j++) {
                sr.d[j] = data[i + j];
            }
            // sr.print();
            dataVal.add(sr);
        }
        int index = 0;
        for (int i = 1; i < dataVal.size(); i++) {
            finalRow[index] = (getDistance(dataVal.get(0).d, dataVal.get(i).d) / 600);
            finalRow[index + 15] = (thetaAngle(dataVal.get(0).d, dataVal.get(i).d) / 150);
            index++;
            //   System.out.println("INDEX:   "+index );
        }

        //  System.out.println("GOT CURRENT INDEX:   "+index );

        for (int i = 1; i < dataVal.size(); i++) {
            for (int j = i + 1; j < dataVal.size(); j++) {
                finalRow[index] = (getDistance(dataVal.get(i).d, dataVal.get(j).d) / 600);
                finalRow[index + 15] = (thetaAngle(dataVal.get(i).d, dataVal.get(j).d) / 150);
                index++;


            }
        }

//        System.out.println(" INDEX:   " + index + "  \n");
//
//        for (int i = 0; i < finalRow.length; i++) {
//            System.out.println("  " + finalRow[i]);
//        }
        return finalRow;
    }

    double[] getFeaturesLeftHand(double data[]) {
        double finalRow[] = new double[30];
        Vector<SingleDistanceFeature> dataVal = new Vector<>();
        for (int i = 0; i < 18; i = i + 3) {
            SingleDistanceFeature sr = new SingleDistanceFeature();
            for (int j = 0; j < 3; j++) {
                sr.d[j] = data[i + j];
            }
            // sr.print();
            dataVal.add(sr);
        }
        int index = 0;
        for (int i = 1; i < dataVal.size(); i++) {
            finalRow[index] = (getDistance(dataVal.get(0).d, dataVal.get(i).d) / 600);
            finalRow[index + 15] = (thetaAngle(dataVal.get(0).d, dataVal.get(i).d) / 150);
            index++;
            //   System.out.println("INDEX:   "+index );
        }

        //  System.out.println("GOT CURRENT INDEX:   "+index );

        for (int i = 1; i < dataVal.size(); i++) {
            for (int j = i + 1; j < dataVal.size(); j++) {
                finalRow[index] = (getDistance(dataVal.get(i).d, dataVal.get(j).d) / 600);
                finalRow[index + 15] = (thetaAngle(dataVal.get(i).d, dataVal.get(j).d) / 150);
                index++;


            }
        }

//        System.out.println(" INDEX:   " + index + "  \n");
//
//        for (int i = 0; i < finalRow.length; i++) {
//            System.out.println("  " + finalRow[i]);
//        }
        return finalRow;
    }

    double distanceBetweenTwoPoints(double data1[], double data2[]) {


        Vector<SingleDistanceFeature> dataVal1 = new Vector<>();
        Vector<SingleDistanceFeature> dataVal2 = new Vector<>();
        for (int i = 0; i < 1; i++) {
            SingleDistanceFeature sr = new SingleDistanceFeature();
            for (int j = 0; j < 3; j++) {
                sr.d[j] = data1[i + j];
            }
            // sr.print();
            dataVal1.add(sr);
        }


        for (int i = 0; i < 1; i++) {
            SingleDistanceFeature sr = new SingleDistanceFeature();
            for (int j = 0; j < 3; j++) {
                sr.d[j] = data2[i + j];
            }
            // sr.print();
            dataVal2.add(sr);
        }
        return (getDistance(dataVal1.get(0).d, dataVal2.get(0).d) / 5000);
    }

    double thetaAngle(double d1[], double d2[]) {

        double cosVal = 0;
        for (int i = 0; i < 3; i++) {
            cosVal += (d1[i] * d2[i]);
        }
        double div1 = 0, div2 = 0;

        for (int i = 0; i < 3; i++) {
            div1 += (d1[i] * d1[i]);
        }
        for (int i = 0; i < 3; i++) {
            div2 += (d2[i] * d2[i]);
        }
        double fin = Math.sqrt(div1) * Math.sqrt(div2);
        cosVal = (cosVal / fin);
        double finData = Math.acos(cosVal) * 180 / 3.1416;
        return finData;

    }

    double getDistance(double d1[], double d2[]) {
        double dist = 0;
        for (int i = 0; i < d1.length; i++) {
            dist += (d1[i] - d2[i]) * (d1[i] - d2[i]);
        }
        return Math.sqrt(dist);
    }

    void compare() {

        if (GlobalData.currHand == 1) {

            System.out.println("CHECKING:   " + GlobalData.currHand);

        }

    }

//    public void dospeak() {
//
//        try {
//            // Ready to speak  
//            voice.speak(output);
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block  
//            e.printStackTrace();
//        }
//    }
    void initArray() {
        hand1Data[0][0] = jLabel13;
        hand1Data[0][1] = jLabel14;
        hand1Data[0][2] = jLabel15;

        hand1Data[1][0] = jLabel16;
        hand1Data[1][1] = jLabel17;
        hand1Data[1][2] = jLabel18;

        hand1Data[2][0] = jLabel19;
        hand1Data[2][1] = jLabel20;
        hand1Data[2][2] = jLabel21;

        hand1Data[3][0] = jLabel22;
        hand1Data[3][1] = jLabel23;
        hand1Data[3][2] = jLabel24;

        hand1Data[4][0] = jLabel25;
        hand1Data[4][1] = jLabel26;
        hand1Data[4][2] = jLabel27;

        hand1Data[5][0] = jLabel28;
        hand1Data[5][1] = jLabel29;
        hand1Data[5][2] = jLabel30;

        hand2Data[0][0] = jLabel43;
        hand2Data[0][1] = jLabel44;
        hand2Data[0][2] = jLabel45;

        hand2Data[1][0] = jLabel46;
        hand2Data[1][1] = jLabel47;
        hand2Data[1][2] = jLabel48;

        hand2Data[2][0] = jLabel49;
        hand2Data[2][1] = jLabel50;
        hand2Data[2][2] = jLabel51;

        hand2Data[3][0] = jLabel52;
        hand2Data[3][1] = jLabel53;
        hand2Data[3][2] = jLabel54;

        hand2Data[4][0] = jLabel55;
        hand2Data[4][1] = jLabel56;
        hand2Data[4][2] = jLabel57;

        hand2Data[5][0] = jLabel58;
        hand2Data[5][1] = jLabel59;
        hand2Data[5][2] = jLabel60;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel(){
            public void  paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(imgin,0,0,getWidth(),getHeight(),0,0,imgin.getWidth(null),imgin.getHeight(null),null);
            }};
            lblTitle = new javax.swing.JLabel();
            jPanel3 = new javax.swing.JPanel();
            jPanel4 = new javax.swing.JPanel();
            jLabel1 = new javax.swing.JLabel();
            jLabel2 = new javax.swing.JLabel();
            jLabel3 = new javax.swing.JLabel();
            jLabel4 = new javax.swing.JLabel();
            jLabel5 = new javax.swing.JLabel();
            jLabel6 = new javax.swing.JLabel();
            jLabel7 = new javax.swing.JLabel();
            jLabel8 = new javax.swing.JLabel();
            jLabel9 = new javax.swing.JLabel();
            jLabel10 = new javax.swing.JLabel();
            jLabel11 = new javax.swing.JLabel();
            jLabel13 = new javax.swing.JLabel();
            jLabel14 = new javax.swing.JLabel();
            jLabel15 = new javax.swing.JLabel();
            jLabel16 = new javax.swing.JLabel();
            jLabel17 = new javax.swing.JLabel();
            jLabel18 = new javax.swing.JLabel();
            jLabel19 = new javax.swing.JLabel();
            jLabel20 = new javax.swing.JLabel();
            jLabel21 = new javax.swing.JLabel();
            jLabel22 = new javax.swing.JLabel();
            jLabel23 = new javax.swing.JLabel();
            jLabel24 = new javax.swing.JLabel();
            jLabel25 = new javax.swing.JLabel();
            jLabel26 = new javax.swing.JLabel();
            jLabel27 = new javax.swing.JLabel();
            jLabel28 = new javax.swing.JLabel();
            jLabel29 = new javax.swing.JLabel();
            jLabel30 = new javax.swing.JLabel();
            jPanel5 = new javax.swing.JPanel();
            jLabel31 = new javax.swing.JLabel();
            jLabel32 = new javax.swing.JLabel();
            jLabel33 = new javax.swing.JLabel();
            jLabel34 = new javax.swing.JLabel();
            jLabel35 = new javax.swing.JLabel();
            jLabel36 = new javax.swing.JLabel();
            jLabel37 = new javax.swing.JLabel();
            jLabel38 = new javax.swing.JLabel();
            jLabel39 = new javax.swing.JLabel();
            jLabel40 = new javax.swing.JLabel();
            jLabel41 = new javax.swing.JLabel();
            jLabel43 = new javax.swing.JLabel();
            jLabel44 = new javax.swing.JLabel();
            jLabel45 = new javax.swing.JLabel();
            jLabel46 = new javax.swing.JLabel();
            jLabel47 = new javax.swing.JLabel();
            jLabel48 = new javax.swing.JLabel();
            jLabel49 = new javax.swing.JLabel();
            jLabel50 = new javax.swing.JLabel();
            jLabel51 = new javax.swing.JLabel();
            jLabel52 = new javax.swing.JLabel();
            jLabel53 = new javax.swing.JLabel();
            jLabel54 = new javax.swing.JLabel();
            jLabel55 = new javax.swing.JLabel();
            jLabel56 = new javax.swing.JLabel();
            jLabel57 = new javax.swing.JLabel();
            jLabel58 = new javax.swing.JLabel();
            jLabel59 = new javax.swing.JLabel();
            jLabel60 = new javax.swing.JLabel();
            jLabel12 = new javax.swing.JLabel();
            jTextField1 = new javax.swing.JTextField();
            jLabel42 = new javax.swing.JLabel();
            txtspeech = new javax.swing.JTextField();
            jTextField2 = new javax.swing.JTextField();
            jButton1 = new javax.swing.JButton();
            jScrollPane1 = new javax.swing.JScrollPane();
            jList1 = new javax.swing.JList();
            jPanel1 = new javax.swing.JPanel();
            jRadioASL = new javax.swing.JRadioButton();
            jButton2 = new javax.swing.JButton();
            jComboBox1 = new javax.swing.JComboBox();
            jLabel61 = new javax.swing.JLabel();
            jButton3 = new javax.swing.JButton();
            jComboBox2 = new javax.swing.JComboBox();
            jLabel62 = new javax.swing.JLabel();

            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

            jPanel2.setBackground(new java.awt.Color(255, 255, 255));

            lblTitle.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
            lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblTitle.setText("GESTURE RECOGNITION");

            jPanel3.setBackground(new java.awt.Color(255, 255, 255));
            jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

            jPanel4.setBackground(new java.awt.Color(255, 255, 255));
            jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

            jLabel1.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel1.setText("HAND 1");

            jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
            jLabel2.setText("CENTER : ");

            jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
            jLabel3.setText("TYPE_THUMB");

            jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
            jLabel4.setText("TYPE_INDEX");

            jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
            jLabel5.setText("TYPE_MIDDLE");

            jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
            jLabel6.setText("TYPE_RING");

            jLabel7.setText(" ");

            jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
            jLabel8.setText("TYPE_PINKY");

            jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel9.setText("X");

            jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel10.setText("Y");

            jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel11.setText("Z");

            jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel13.setText("X");

            jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel14.setText("Y");

            jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel15.setText("Z");

            jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel16.setText("X");

            jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel17.setText("Y");

            jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel18.setText("Z");

            jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel19.setText("X");

            jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel20.setText("Y");

            jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel21.setText("Z");

            jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel22.setText("X");

            jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel23.setText("Y");

            jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel24.setText("Z");

            jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel25.setText("X");

            jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel26.setText("Y");

            jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel27.setText("Z");

            jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel28.setText("X");

            jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel29.setText("Y");

            jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel30.setText("Z");

            javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
            jPanel4.setLayout(jPanel4Layout);
            jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(jLabel9)
                        .addComponent(jLabel10)
                        .addComponent(jLabel11))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addComponent(jLabel2))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addComponent(jLabel3))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21))
                        .addComponent(jLabel4))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24))
                        .addComponent(jLabel5))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27))
                        .addComponent(jLabel6))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30))
                        .addComponent(jLabel8))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jPanel5.setBackground(new java.awt.Color(255, 255, 255));
            jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

            jLabel31.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
            jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel31.setText("HAND 2");

            jLabel32.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
            jLabel32.setText("CENTER : ");

            jLabel33.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
            jLabel33.setText("TYPE_THUMB");

            jLabel34.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
            jLabel34.setText("TYPE_INDEX");

            jLabel35.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
            jLabel35.setText("TYPE_MIDDLE");

            jLabel36.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
            jLabel36.setText("TYPE_RING");

            jLabel37.setText(" ");

            jLabel38.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
            jLabel38.setText("TYPE_PINKY");

            jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel39.setText("X");

            jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel40.setText("Y");

            jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel41.setText("Z");

            jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel43.setText("X");

            jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel44.setText("Y");

            jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel45.setText("Z");

            jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel46.setText("X");

            jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel47.setText("Y");

            jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel48.setText("Z");

            jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel49.setText("X");

            jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel50.setText("Y");

            jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel51.setText("Z");

            jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel52.setText("X");

            jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel53.setText("Y");

            jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel54.setText("Z");

            jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel55.setText("X");

            jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel56.setText("Y");

            jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel57.setText("Z");

            jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel58.setText("X");

            jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel59.setText("Y");

            jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel60.setText("Z");

            javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
            jPanel5.setLayout(jPanel5Layout);
            jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel31)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel37)
                        .addComponent(jLabel39)
                        .addComponent(jLabel40)
                        .addComponent(jLabel41))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel32)
                        .addComponent(jLabel43)
                        .addComponent(jLabel44)
                        .addComponent(jLabel45))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel33)
                        .addComponent(jLabel46)
                        .addComponent(jLabel47)
                        .addComponent(jLabel48))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel34)
                        .addComponent(jLabel49)
                        .addComponent(jLabel50)
                        .addComponent(jLabel51))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel35)
                        .addComponent(jLabel52)
                        .addComponent(jLabel53)
                        .addComponent(jLabel54))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel36)
                        .addComponent(jLabel55)
                        .addComponent(jLabel56)
                        .addComponent(jLabel57))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel38)
                        .addComponent(jLabel58)
                        .addComponent(jLabel59)
                        .addComponent(jLabel60))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
            jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel12.setText("GESTURE NAME");

            jTextField1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
            jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

            jLabel42.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
            jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel42.setText("Speech");

            txtspeech.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
            jPanel3.setLayout(jPanel3Layout);
            jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextField2)
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField1)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtspeech))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addContainerGap())
            );
            jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtspeech)
                        .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addContainerGap())
            );

            jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
            jButton1.setText("BACK");
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

            jList1.setModel(new javax.swing.AbstractListModel() {
                String[] strings = { " " };
                public int getSize() { return strings.length; }
                public Object getElementAt(int i) { return strings[i]; }
            });
            jScrollPane1.setViewportView(jList1);

            jPanel1.setBackground(new java.awt.Color(255, 255, 255));
            jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

            buttonGroup1.add(jRadioASL);
            jRadioASL.setText("ASL");

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jRadioASL)
                    .addContainerGap(583, Short.MAX_VALUE))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jRadioASL)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jButton2.setText("Translate");
            jButton2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });

            jLabel61.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
            jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel61.setText("Select Langauge:");

            jButton3.setText("Clear");
            jButton3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton3ActionPerformed(evt);
                }
            });

            jLabel62.setText("Delays:");

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(22, 22, 22)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jScrollPane1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                                .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox2))
                    .addContainerGap())
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        t.cancel();
        this.setVisible(false);
        parent.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    public void voiceOutput(String str) {


        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "\\Reader\\incoming.txt"));
            bw.write(str);
            bw.close();
            try {
                new ProcessBuilder(System.getProperty("user.dir") + "\\READER\\READER.EXE").start();


                Thread.sleep(1800);

                try {
//            Thread.sleep(100);
                    new ProcessBuilder("taskkill", "/IM", "reader.exe").start();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {;
            }



        } catch (Exception e) {
            System.out.println("Exception Writing Voice Command: " + e);
        }

    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String out = jTextField2.getText();
        if (out.equals("")) {
            JOptionPane.showMessageDialog(this, "Empty Data!!");
            return;
        }
        if (!out.equals("")) {

            try {
                // System.out.println("Index" + jComboBox1.getSelectedIndex());
                String outSpa = Translate.execute(out, Language.AUTO_DETECT, langArray[jComboBox1.getSelectedIndex()]);
                System.out.println("output: " + outSpa);
                voiceOutput(outSpa);
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
        output="";
        finalString="";
        jTextField2.setText("");

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
       finalString="";
        output = "";
        jTextField2.setText("");
        jTextField1.setText("");


    }//GEN-LAST:event_jButton3ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton jRadioASL;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtspeech;
    // End of variables declaration//GEN-END:variables
}
