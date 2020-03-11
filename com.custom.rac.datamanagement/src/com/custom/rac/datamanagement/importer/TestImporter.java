package com.custom.rac.datamanagement.importer;

import java.util.ArrayList;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class TestImporter extends AbstractImporter {

	TCComponentItemType itemType;
	@Override
	public TCComponentItemType getItemType(int index) {
		
		if(itemType == null) {
			TCSession session = (TCSession) AIFUtility.getDefaultSession();
			try {
				itemType = (TCComponentItemType) session.getTypeComponent("SF8_PPart");
			} catch (TCException e) {
				e.printStackTrace();
			}
		}
		return itemType;
	}

	@Override
	public void onSingleStart(int index) {
		System.out.println("�� " + index + " �п�ʼ���룡");
	}

	@Override
	public void onSingleFinish(int index) {
		System.out.println("�� " + index + " �е�����ϣ�");
	}

	@Override
	public void onStart() {
		System.out.println("��ʼ����");
	}

	@Override
	public void onFinish() {
		System.out.println("��ɵ���");
	}

	@Override
	public String getName() {
		return "���Եĵ��빤��";
	}

	@Override
	public void onSingleError(int index, Exception e) {
		System.out.println("�� " + index +"�����ˣ�" + e.toString());
	}
	
	static ArrayList<String> ignoreList = new ArrayList<String>();
	static{
		ignoreList.add("���");
	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) {
		return ignoreList.contains(propertyDisplayName);
	}

	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) {
		
	}

	@Override
	public PropertyContainer getPropertyContainer(int index) {
		return PropertyContainer.itemRevision;
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean ignoreRow(int index) {
		return false;
	}

	@Override
	public boolean deleteOldItemWhenItemIdExist(int index) {
		return true;
	}

}
