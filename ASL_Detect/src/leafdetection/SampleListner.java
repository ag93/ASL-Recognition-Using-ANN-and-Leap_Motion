package leafdetection;

/**
 * ****************************************************************************\
 * Copyright (C) 2012-2013 Leap Motion, Inc. All rights reserved. * Leap Motion
 * proprietary and confidential. Not for distribution. * Use subject to the
 * terms of the Leap Motion SDK Agreement available at *
 * https://developer.leapmotion.com/sdk_agreement, or another agreement *
 * between Leap Motion and you, your company or other organization. *
 * \*****************************************************************************
 */
import java.lang.Math;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class SampleListner extends Listener {

    String filepath;
    BufferedWriter bwStart, bwEnd;
    public SampleListner listener;
    public Controller controller;
    public static int currHand = -1;

    public void onInit(Controller controller) {
        System.out.println("Initialized");
        try {
            filepath = System.getProperty("user.dir") + "\\data\\";
            bwStart = new BufferedWriter(new FileWriter(new File(filepath + "start.txt")));
            bwEnd = new BufferedWriter(new FileWriter(new File(filepath + "end.txt")));
        } catch (Exception e) {
            System.out.println();
        }
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        String s;
        StringTokenizer stok;
        double d1, d2, d3;
        int count = 0;
        try {
            count = 0;
            Frame frame = controller.frame();
            int cnt = 0;
            ArrayList<String> allhands = new ArrayList<>();
            currHand = -1;
            for (Hand hand : frame.hands()) {
                cnt++;
                String handType = hand.isLeft() ? "Left hand" : "Right hand";
                allhands.add(handType);


                if (hand.isLeft()) {
                    //  System.out.println("Only Left Hand !!!");
                    s = hand.palmPosition().toString().substring(1, hand.palmPosition().toString().length() - 1);
                    stok = new StringTokenizer(s, ",");
                    GlobalData.data[0][0] = Double.parseDouble(stok.nextToken().trim());
                    GlobalData.data[0][1] = Double.parseDouble(stok.nextToken().trim());
                    GlobalData.data[0][2] = Double.parseDouble(stok.nextToken().trim());
                    for (Finger finger : hand.fingers()) {
                        //Get Bones
                        if (finger.type().equals(Finger.Type.TYPE_THUMB)) {
                            Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
                            s = bone.nextJoint().toString().substring(1, bone.nextJoint().toString().length() - 1);
                            stok = new StringTokenizer(s, ",");
                            GlobalData.data[0][3] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[0][4] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[0][5] = Double.parseDouble(stok.nextToken().trim());
                        } else if (finger.type().equals(Finger.Type.TYPE_INDEX)) {
                            Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
                            s = bone.nextJoint().toString().substring(1, bone.nextJoint().toString().length() - 1);
                            stok = new StringTokenizer(s, ",");
                            GlobalData.data[0][6] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[0][7] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[0][8] = Double.parseDouble(stok.nextToken().trim());
                        } else if (finger.type().equals(Finger.Type.TYPE_MIDDLE)) {
                            Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
                            s = bone.nextJoint().toString().substring(1, bone.nextJoint().toString().length() - 1);
                            stok = new StringTokenizer(s, ",");
                            GlobalData.data[0][9] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[0][10] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[0][11] = Double.parseDouble(stok.nextToken().trim());
                        } else if (finger.type().equals(Finger.Type.TYPE_RING)) {
                            Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
                            s = bone.nextJoint().toString().substring(1, bone.nextJoint().toString().length() - 1);
                            stok = new StringTokenizer(s, ",");
                            GlobalData.data[0][12] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[0][13] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[0][14] = Double.parseDouble(stok.nextToken().trim());
                        } else if (finger.type().equals(Finger.Type.TYPE_PINKY)) {
                            Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
                            s = bone.nextJoint().toString().substring(1, bone.nextJoint().toString().length() - 1);
                            stok = new StringTokenizer(s, ",");
                            GlobalData.data[0][15] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[0][16] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[0][17] = Double.parseDouble(stok.nextToken().trim());
                        }
                    }
                } else {
                    //  System.out.println("Only Right Hand !!!");
                    s = hand.palmPosition().toString().substring(1, hand.palmPosition().toString().length() - 1);
                    stok = new StringTokenizer(s, ",");
                    GlobalData.data[1][0] = Double.parseDouble(stok.nextToken().trim());
                    //   System.out.println("center: " + GlobalData.data[1][0]);
                    GlobalData.data[1][1] = Double.parseDouble(stok.nextToken().trim());
                    GlobalData.data[1][2] = Double.parseDouble(stok.nextToken().trim());
                    for (Finger finger : hand.fingers()) {
                        //Get Bones
                        if (finger.type().equals(Finger.Type.TYPE_THUMB)) {
                            Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
                            s = bone.nextJoint().toString().substring(1, bone.nextJoint().toString().length() - 1);
                            stok = new StringTokenizer(s, ",");
                            GlobalData.data[1][3] = Double.parseDouble(stok.nextToken().trim());
//                            System.out.println("Thumb X : " + GlobalData.data[1][3]);
                            GlobalData.data[1][4] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[1][5] = Double.parseDouble(stok.nextToken().trim());
//                            System.out.println("Thumb X : " + GlobalData.data[1][3]);
//                            System.out.println("Thumb Y : " + GlobalData.data[1][4]);
//                            System.out.println("Thumb Z : " + GlobalData.data[1][5]);

                        } else if (finger.type().equals(Finger.Type.TYPE_INDEX)) {
                            Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
                            s = bone.nextJoint().toString().substring(1, bone.nextJoint().toString().length() - 1);
                            stok = new StringTokenizer(s, ",");
                            GlobalData.data[1][6] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[1][7] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[1][8] = Double.parseDouble(stok.nextToken().trim());
//                            System.out.println("TYPE_INDEX X: " + GlobalData.data[1][6]);
//                            System.out.println("TYPE_INDEX Y: " + GlobalData.data[1][7]);
//                            System.out.println("TYPE_INDEX Z: " + GlobalData.data[1][8]);
                        } else if (finger.type().equals(Finger.Type.TYPE_MIDDLE)) {
                            Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
                            s = bone.nextJoint().toString().substring(1, bone.nextJoint().toString().length() - 1);
                            stok = new StringTokenizer(s, ",");
                            GlobalData.data[1][9] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[1][10] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[1][11] = Double.parseDouble(stok.nextToken().trim());
                        } else if (finger.type().equals(Finger.Type.TYPE_RING)) {
                            Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
                            s = bone.nextJoint().toString().substring(1, bone.nextJoint().toString().length() - 1);
                            stok = new StringTokenizer(s, ",");
                            GlobalData.data[1][12] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[1][13] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[1][14] = Double.parseDouble(stok.nextToken().trim());
                        } else if (finger.type().equals(Finger.Type.TYPE_PINKY)) {
                            Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
                            s = bone.nextJoint().toString().substring(1, bone.nextJoint().toString().length() - 1);
                            stok = new StringTokenizer(s, ",");
                            GlobalData.data[1][15] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[1][16] = Double.parseDouble(stok.nextToken().trim());
                            GlobalData.data[1][17] = Double.parseDouble(stok.nextToken().trim());
//                            System.out.println("PINKY X: " + GlobalData.data[1][15]);
//                            System.out.println("PINKY Y: " + GlobalData.data[1][16]);
//                            System.out.println("PINKY Z: " + GlobalData.data[1][17]);
                        }
                    }
                }
            }
            //  System.out.println("cnt: " + cnt);
            if (cnt == 2) {
                // System.out.println("hand both hands");
                currHand = 2;
            } else {
                if (allhands.size() > 0) {
                    //  System.out.println("handtype !!!" + allhands.get(0));
                    if (allhands.get(0).equals("Left hand")) {
                        currHand = 1;
                    } else {
                        currHand = 0;
                    }
                }

            }
            GlobalData.currHand = currHand;
            Thread.sleep(5);
        } catch (Exception e) {
            System.out.println("Error : " + e);
            e.printStackTrace();
            System.exit(0);
        }
    }
}
