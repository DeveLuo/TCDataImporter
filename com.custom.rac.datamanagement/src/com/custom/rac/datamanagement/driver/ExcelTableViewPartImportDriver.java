package com.custom.rac.datamanagement.driver;

import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.custom.rac.datamanagement.swtxls.MyCell;
import com.custom.rac.datamanagement.swtxls.SWTSheet;
import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.IImporter;
import com.custom.rac.datamanagement.util.MyCharNumber;
import com.custom.rac.datamanagement.util.RunState;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;

public class ExcelTableViewPartImportDriver implements IImportDriver{

	private int startRowNum;
	private ExcelTableViewPart tableViewPart;
	private AbstractImporter importer;
	
	public ExcelTableViewPartImportDriver(ExcelTableViewPart tableViewPart, int startRowNum){
		this.tableViewPart = tableViewPart;
		this.startRowNum = startRowNum;
	}
	
	public ExcelTableViewPart getTableViewPart() {
		return tableViewPart;
	}
	
	@Override
	public void onSingleStart(int index) {
		System.out.println("���������ϵ�����(��ʼ)��" + index);
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewPart.getSWTWorkbook().getSelectedSheet().setState(index+startRowNum, RunState.running);
			}
		});
	}

	@Override
	public void onSingleFinish(int index) {
		System.out.println("���������ϵ�����(���)��" + index);
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewPart.setProgressValue(index+startRowNum);
				tableViewPart.getSWTWorkbook().getSelectedSheet().setState(index+startRowNum, RunState.finish);
				tableViewPart.getSWTWorkbook().getSelectedSheet().setInfomation(index+startRowNum, "�������");
			}
		});
		
	}

	@Override
	public void onSingleError(int index, Exception e) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewPart.setProgressValue(index+startRowNum);
				tableViewPart.getSWTWorkbook().getSelectedSheet().setState(index+startRowNum, RunState.error);
				tableViewPart.getSWTWorkbook().getSelectedSheet().setInfomation(index+startRowNum, e.getMessage());
			}
		});
	}

	@Override
	public void onStart() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewPart.initProgressBar();
				tableViewPart.setExecuting(true);
			}
		});
	}

	@Override
	public void onFinish() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewPart.setExecuting(false);
			}
		});
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) {
	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) {
		
	}

	@Override
	public void onNewItemId(int index, String itemId) {
		setValueWithRealName(index, "item_id", itemId);
	}
	

	@Override
	public void onNewItemRevId(int index, String itemRevId) {
		setValueWithRealName(index, "item_revision_id", itemRevId);
	}

	public void setValueWithRealName(int index, String propRealName, String propValue) {
		
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				SWTSheet swtSheet = tableViewPart.getSWTWorkbook().getSelectedSheet();
				Table table = swtSheet.getTable();
				TableItem tableItem = table.getItem(index + startRowNum);
				List<MyCell> titleCells = swtSheet.getSheet().rows.get(startRowNum-1).cells;
				try {
					String displayName = importer.getDisplayNameFromRealName(index, propRealName);
					int columnNum = -1;
					for (MyCell titleCell : titleCells) {
						if(titleCell != null && titleCell.value.equals(displayName)) {
							columnNum = new MyCharNumber(titleCell.cellReference).getValue();
							break;
						}
					}
					if(columnNum > 0) {
						//cell�л�ȡ���к��Ǵ�1��ʼ�ģ�����Ҫ��-1��Ȼ�����ǰ���б��̶���ʾ
						tableItem.setText(columnNum - 1 + 2, propValue);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	@Override
	public void setImporter(IImporter importer) {
		this.importer = (AbstractImporter) importer;
	}

}
