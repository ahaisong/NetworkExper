package jiwang;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSeparator;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

//import netcap.*;
import jpcap.*;
import jpcap.packet.*;
import java.util.*;
import java.sql.Timestamp;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class JFrameMain extends javax.swing.JFrame implements ActionListener {

	private JMenuItem exitMenuItem;
	private JSeparator jSeparator2;

	private JMenuItem stopMenuItem;
	private JMenuItem startMenuItem;
	private JMenu Menu;
	private JMenuBar jMenuBar1;
	//10-10new menu
	private JMenuItem newSave;
	private JMenuItem newAnalysis;
	private JMenuItem newChart;
	//end
	
	JTable tabledisplay = null;
	Vector rows, columns;
	DefaultTableModel tabModel;
	JScrollPane scrollPane;
	JLabel statusLabel;

	Netcaptor captor = new Netcaptor();

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		JFrameMain inst = new JFrameMain();
		inst.setVisible(true);
	}

	public JFrameMain() {
		super();
		initGUI();
		this.setLocationRelativeTo(null);
	}

	private void initGUI() {
		try {
			setSize(800, 600);
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
				
					//���ò˵�
					Menu = new JMenu();
					jMenuBar1.add(Menu);
					Menu.setText("�˵�");
					Menu.setPreferredSize(new java.awt.Dimension(35, 21));
					{
						startMenuItem = new JMenuItem();
						Menu.add(startMenuItem);
						startMenuItem.setText("��ʼ");
						startMenuItem.setActionCommand("start");
						startMenuItem.addActionListener(this);
					}
					{
						stopMenuItem = new JMenuItem();
						Menu.add(stopMenuItem);
						stopMenuItem.setText("ֹͣ");
						stopMenuItem.setActionCommand("stop");
						stopMenuItem.addActionListener(this);
					}
					{
						newAnalysis = new JMenuItem();
						Menu.add(newAnalysis);
						newAnalysis.setText("���ͳ��");
						newAnalysis.setActionCommand("newanalysis");
						newAnalysis.addActionListener(this);
					}
					//10-10 new add.
					{
						newChart=new JMenuItem();
						Menu.add(newChart);
						newChart.setText("ͼ����ʾ");
						newChart.setActionCommand("newchart");
						newChart.addActionListener(this);
					}
					{
						newSave=new JMenuItem();
						Menu.add(newSave);
						newSave.setText("�������");
						newSave.setActionCommand("save");
						newSave.addActionListener(this);
					}
					{
						jSeparator2 = new JSeparator();
						Menu.add(jSeparator2);
					}
					{
						exitMenuItem = new JMenuItem();
						Menu.add(exitMenuItem);
						exitMenuItem.setText("Exit");
						exitMenuItem.setActionCommand("exit");
						exitMenuItem.addActionListener(this);
					}
					
				}
			}

			rows = new Vector();
			columns = new Vector();

			columns.addElement("���ݱ�ʱ��");
			columns.addElement("ԴIP��ַ");
			columns.addElement("Ŀ��IP��ַ");
			columns.addElement("�ײ�����");
			columns.addElement("���ݳ���");
			columns.addElement("�Ƿ�ֶ�");
			columns.addElement("�ֶ�ƫ����");
			columns.addElement("�ײ�����");
			columns.addElement("��������");

			tabModel = new DefaultTableModel();
			tabModel.setDataVector(rows, columns);
			tabledisplay = new JTable(tabModel);
//			scrollPane = new JScrollPane(tabledisplay);

			statusLabel = new JLabel("");
			this.getContentPane().add(statusLabel, BorderLayout.SOUTH);
			{
				getContentPane().add(new JScrollPane(tabledisplay), BorderLayout.CENTER);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent event) {
		String cmd = event.getActionCommand();
//		System.out.println(cmd);
		if (cmd.equals("start")) {
			captor.capturePacketsFromDevice();
			captor.setJFrame(this);
		} else if (cmd.equals("stop")) {
			captor.stopCapture();
		} else if (cmd.equals("exit")) {
			System.exit(0);
		}else if(cmd.equals("newanalysis")) {
			new newCount(this).setVisible(true);
		}else if(cmd.equals("save")) {
			//����
			String str="tcp:"+newCount.ctcp+"	udp:"+newCount.cudp+"	icmp:"+newCount.cicmp+
					"	arp:"+newCount.carp+"	�㲥��:"+newCount.cGuangBo;
			SaveFile sf = new SaveFile();
			sf.saveFile(this, str);
		}else if(cmd.equals("newchart")) {
			new  newChart1(this).setVisible(true);
		}
	}

	@SuppressWarnings("unchecked")
	public void dealPacket(Packet packet) {
		try {
			Vector r = new Vector();
			String strtmp;
			Timestamp timestamp = new Timestamp((packet.sec * 1000) + (packet.usec / 1000));

			r.addElement(timestamp.toString()); // ���ݱ�ʱ��
			r.addElement(((IPPacket) packet).src_ip.toString()); // ԴIP��ַ
			r.addElement(((IPPacket) packet).dst_ip.toString()); // Ŀ��IP��ַ
			if(((IPPacket) packet).dst_ip.toString().equals("255.255.255.255")) {
				newCount.cGuangBo++;
				newCount.dGuangBo+=(double)packet.len/1024;
			}
			r.addElement(packet.header.length); // �ײ�����
			r.addElement(packet.data.length); // ���ݳ���
			r.addElement(((IPPacket) packet).dont_frag == true ? "�ֶ�" : "���ֶ�"); // �Ƿ񲻷ֶ�
			r.addElement(((IPPacket) packet).offset); // ���ݳ���

			strtmp = "";
			for (int i = 0; i < packet.header.length; i++) {
				strtmp += Byte.toString(packet.header[i]);
			}
			r.addElement(strtmp); // �ײ�����

			strtmp = "";
			for (int i = 0; i < packet.data.length; i++) {
				strtmp += Byte.toString(packet.data[i]);
			}
			r.addElement(strtmp); // ��������

			rows.addElement(r);
			tabledisplay.addNotify();
		} catch (Exception e) {

		}
	}
}