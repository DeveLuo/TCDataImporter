package com.custom.rac.datamanagement.action;

import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;

public class ExportTableAction extends AbstractTableAction {

	public ExportTableAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {

		//���жϳ����Ƿ��ڽ�����
		boolean isExecuting = tableViewPart.isExecuting();
		if(isExecuting) {
			throw new Exception("����ִ���У��޷���������888��");
		}
		
		String lastSelectedFilePath = OpenFileAction.lastSelectFile;
	}

}
