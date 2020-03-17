package com.custom.rac.datamanagement.driver;

public interface IImportDriver {

	void onSingleStart(int index);
	
	void onSingleFinish(int index);
	
	void onSingleError(int index, Exception e);
	
	void onStart();
	
	void onFinish();
	
	void onSetPropertyFinish(int index, String propertyDisplayName);
	
	void onSetPropertyError(int index, String propertyDisplayName, Exception e);
	
	void onNewItemId(int index, String itemId);
	
	void onNewItemRevId(int index, String itemId);
}
