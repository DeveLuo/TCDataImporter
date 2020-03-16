package com.custom.rac.datamanagement.importer;

import java.util.ArrayList;
import java.util.HashMap;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class SFGKPartImporter extends AbstractImporter {
	
	private static HashMap<String, String> typeMap = new HashMap<String, String>();
	static {
		typeMap.put("��Ʒ", "SF8_PPart");
		typeMap.put("���Ʒ", "SF8_SPart");
		typeMap.put("ë��", "SF8_Wpart");
		typeMap.put("ԭ����", "SF8_RPart");
		typeMap.put("���", "SF8_FPart");	
	}
	
	@Override
	public String getName() {
		return "�Ϸ�߿����ϵ������";
	}

	@Override
	public void execute() throws Exception {
		super.execute();
	}
	

	@Override
	public TCComponentItemType getItemType(int index) {
		String type = (String) getValue(index, "��������");
		String reltype = typeMap.get(type);
		TCSession session = (TCSession) AIFUtility.getDefaultSession();
		TCComponentItemType itemType = null;
		try {		
			itemType = (TCComponentItemType) session.getTypeComponent(reltype);
		} catch (TCException e) {
			e.printStackTrace();
		}	
		return itemType;
	}
	
	@Override
	public void setValue(TCComponent tcComponent, int index, String propertyDisplayName) throws Exception {
		
		super.setValue(tcComponent, index, propertyDisplayName);
		
	}

	@Override
	public PropertyContainer getPropertyContainer(int index) {
		return PropertyContainer.itemRevision;
	}

	@Override
	public void onSingleStart(int index) {
		System.out.println("�� " + index + " �п�ʼ���룡");
	}

	@Override
	public void onSingleFinish(int index, TCComponent tcc) throws Exception{
		System.out.println("�� " + index + " �е�����ϣ�");
	}
	

	@Override
	public void onSingleError(int index, Exception e) {
		System.out.println("�� " + index +"�����ˣ�" + e.toString());
	}

	@Override
	public void onStart() {
		System.out.println("���뿪ʼ");
	}

	@Override
	public void onFinish() {
		System.out.println("�����������鿴������־");
	}
	
	static ArrayList<String> ignoreList = new ArrayList<String>();
	static{
		ignoreList.add("���");
		ignoreList.add("���ϱ���");
		ignoreList.add("����");
	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) {
		return ignoreList.contains(propertyDisplayName);
	}

	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) {
		
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) {
		
	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) {
		
	}

	@Override
	public boolean ignoreRow(int index) {
		return false;
	}

	@Override
	public boolean deleteOldItemWhenItemIdExist(int index) {
		return false;
	}

}
