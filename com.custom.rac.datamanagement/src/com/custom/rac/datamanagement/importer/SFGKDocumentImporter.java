package com.custom.rac.datamanagement.importer;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCSession;

public class SFGKDocumentImporter extends AbstractImporter {

	@Override
	public String getName() {
		return "�Ϸ�߿��ĵ��������";
	}

	@Override
	public void execute() {
		System.out.println("ִ�� �� " + getName());
	}

	@Override
	public TCComponentItemType getItemType(int index) throws Exception{
		TCSession session = (TCSession) AIFUtility.getDefaultSession();
		TCComponentItemType type = (TCComponentItemType) session.getTypeComponent("SF8_Document");
		return type;
	}

	@Override
	public PropertyContainer getPropertyContainer(int index) {
		return PropertyContainer.itemRevision;
	}

	@Override
	public void onSingleStart(int index) {
		
	}

	@Override
	public void onSingleFinish(int index, TCComponent tcc) throws Exception{
		System.out.println("�ڽ�����Ҫ�������ݼ�");
	}

	@Override
	public void onSingleError(int index, Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) {
		if (propertyDisplayName.equals("�ĵ����") || propertyDisplayName.equals("�汾") 
			|| propertyDisplayName.equals("����") || propertyDisplayName.equals("�ĵ�����")
			|| propertyDisplayName.equals("�ĵ�����ID") || propertyDisplayName.equals("���ӵ���ŵ�ַ")){
			return true;
		}
		return false;
	}

	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) {
		System.out.println("��" + index + "��");
		
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
		return false;
	}

}
