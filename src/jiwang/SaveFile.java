package jiwang;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * ���ڱ����ļ�����
 * 
 * @author ������
 *
 */
public class SaveFile {
	/**
	 * �������ݵ��ļ�
	 * 
	 * @param content
	 *            ����
	 */
	public void saveFile(Component parent, String content) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);
		fc.setMultiSelectionEnabled(false);
		int result = fc.showSaveDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();

			if (!file.getPath().endsWith(".txt")) {
				file = new File(file.getPath() + ".txt");
			}
			// System.out.println("file path=" + file.getPath());

			FileOutputStream fos = null;

			try {
				if (!file.exists()) {
					file.createNewFile();
				}

				fos = new FileOutputStream(file);
				fos.write(content.getBytes());
				fos.flush();

				// ������ʾ�ɹ�
				JOptionPane.showMessageDialog(parent, "�ļ�����ɹ���·����" + file.getPath(), "�ɹ�",
						JOptionPane.INFORMATION_MESSAGE);

			} catch (IOException e) {
				// System.out.println("�ļ�����ʧ�ܣ�");
				// ������ʾʧ��
				JOptionPane.showMessageDialog(parent, "�ļ�����ʧ��", "����", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
