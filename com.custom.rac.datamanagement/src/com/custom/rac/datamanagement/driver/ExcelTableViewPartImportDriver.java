package com.custom.rac.datamanagement.driver;

import org.eclipse.swt.widgets.Display;

import com.custom.rac.datamanagement.util.RunState;
import com.custom.rac.datamanagement.views.ExcelTableViewPart;

public class ExcelTableViewPartImportDriver implements IImportDriver{

	private int startRowNum;
	private ExcelTableViewPart tableViewPart;
	
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
			}
		});
	}

	@Override
	public void onStart() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewPart.initProgressBar();
			}
		});
	}

	@Override
	public void onFinish() {
		
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) {
	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) {
		
	}

}
