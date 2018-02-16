VERSION 5.00
Object = "{EEE78583-FE22-11D0-8BEF-0060081841DE}#1.0#0"; "XVoice.dll"
Object = "{3B7C8863-D78F-101B-B9B5-04021C009402}#1.2#0"; "RICHTX32.OCX"
Begin VB.Form MailReader 
   Caption         =   "Reader"
   ClientHeight    =   1620
   ClientLeft      =   60
   ClientTop       =   450
   ClientWidth     =   7065
   LinkTopic       =   "Form1"
   ScaleHeight     =   1620
   ScaleWidth      =   7065
   StartUpPosition =   2  'CenterScreen
   Begin VB.CommandButton Command2 
      Caption         =   "STOP && SKIP"
      Height          =   375
      Left            =   1320
      TabIndex        =   4
      Top             =   600
      Width           =   5655
   End
   Begin VB.Timer Timer1 
      Interval        =   1500
      Left            =   5400
      Top             =   3000
   End
   Begin VB.CommandButton Command1 
      Caption         =   "STOP && EXIT"
      Height          =   375
      Left            =   1320
      TabIndex        =   3
      Top             =   1200
      Width           =   5655
   End
   Begin RichTextLib.RichTextBox rtb1 
      Height          =   5175
      Left            =   120
      TabIndex        =   2
      Top             =   1080
      Visible         =   0   'False
      Width           =   6855
      _ExtentX        =   12091
      _ExtentY        =   9128
      _Version        =   393217
      TextRTF         =   $"MailReader.frx":0000
   End
   Begin ACTIVEVOICEPROJECTLibCtl.DirectSS DirectSS1 
      Height          =   495
      Left            =   120
      OleObjectBlob   =   "MailReader.frx":008B
      TabIndex        =   1
      Top             =   480
      Width           =   855
   End
   Begin VB.Label Label1 
      Alignment       =   2  'Center
      BorderStyle     =   1  'Fixed Single
      Caption         =   "AGENT"
      BeginProperty Font 
         Name            =   "MS Sans Serif"
         Size            =   12
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   375
      Left            =   120
      TabIndex        =   0
      Top             =   120
      Width           =   6855
   End
End
Attribute VB_Name = "MailReader"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Option Explicit
Dim fname As String
Dim toSpeak As String

Private Sub Command1_Click()
DirectSS1.AudioReset
End
End Sub

Private Sub Command2_Click()
DirectSS1.AudioReset
Timer1.Enabled = True
End Sub

Private Sub DirectSS1_AudioStop(ByVal hi As Long, ByVal lo As Long)
 Timer1.Enabled = True
End Sub

Private Sub Timer1_Timer()
fname = App.Path & "\incoming.txt"
If (Dir(fname) <> "") Then
 rtb1.LoadFile fname, 1
 toSpeak = rtb1.Text
 DirectSS1.Speak toSpeak
 Kill fname
 Timer1.Enabled = False
End If


End Sub
