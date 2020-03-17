package com.custom.rac.datamanagement.action;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;

import com.custom.rac.datamanagement.util.AbstractTableAction;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;

public class SaveResultAction extends AbstractTableAction {

	public SaveResultAction(ExcelTableViewPart tableViewPart) {
		super(tableViewPart);
	}

	@Override
	public void run(Widget widget) throws Exception {

		//���жϳ����Ƿ��ڽ�����
		boolean isExecuting = tableViewPart.isExecuting();
		if(isExecuting) {
			throw new Exception("����ִ���У��޷��������ݣ�");
		}
		
		String lastSelectedFilePath = OpenFileAction.lastSelectFile;
		
		Table table = tableViewPart.getSWTWorkbook().getSelectedSheet().getTable();
	}

}
