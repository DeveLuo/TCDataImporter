package com.custom.rac.datamanagement.action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.util.WriteDataToExcel;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;
import com.teamcenter.rac.util.MessageBox;

public class ExportTableAction extends AbstractTableAction {

	public ExportTableAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {
		// ���жϳ����Ƿ��ڽ�����
		tableViewPart.setExecuting(false);
		boolean isExecuting = tableViewPart.isExecuting();
		if (isExecuting) {
			throw new Exception("����ִ���У��޷��������ݣ�");
		}
		// �ļ���·��
		String lastSelectedFilePath = OpenFileAction.lastSelectFile;
		// �������Ի���
		FileDialog fd = new FileDialog(tableViewPart.getContainer().getShell(), SWT.SAVE);
		fd.setFilterPath(System.getProperty("JAVA.HOME"));
		fd.setFilterExtensions(new String[] { "*.xlsx" });
		fd.setFilterNames(new String[] { "Excel Files(*.xlsx)" });
		// ��������ļ���
		if (lastSelectedFilePath == null) {
			throw new Exception("û�����ݵ�����");
		}
		String fileName = lastSelectedFilePath.substring(lastSelectedFilePath.lastIndexOf("\\") + 1);
		fd.setFileName(fileName);
		String filePath = fd.open();
		if (filePath != null) {
			WriteDataToExcel.WriteData(tableViewPart, lastSelectedFilePath, filePath);
			MessageBox.post("�����ɹ�������·����" + filePath, "��ʾ", MessageBox.INFORMATION);
		}
	}

}
