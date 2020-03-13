package com.custom.rac.datamanagement.importer;

import java.io.File;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.MyClassifyManager;
import com.custom.rac.datamanagement.util.MyDatasetUtil;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCSession;

public class SFGKDocumentImporter extends AbstractImporter {

	String shared_directory_path = "\\\\192.168.25.11";
	MyClassifyManager cls_manger = new MyClassifyManager((TCSession) AIFUtility.getDefaultSession());
	
	
	@Override
	public String getName() {
		return "�Ϸ�߿��ĵ��������";
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
	public void onSingleStart(int index) throws Exception{
		
	}

	@Override
	public void onSingleFinish(int index, TCComponent tcc) throws Exception {
		addClassify(getValue(index, "�ĵ�����ID") + "", tcc);
		String path = (String) getValue(index, "���ӵ���ŵ�ַ");
		if (path != null && path.length() > 0) {
			if (!path.startsWith("\\") && !path.startsWith("/")) {
				path = "\\" + path;
			}
			path = shared_directory_path + path;
			File file = new File(path);
			if (file != null && file.exists() &&file.isFile()) {
				MyDatasetUtil.createDateset(tcc, file.getName(), file, "IMAN_specification");
			} else {
				throw new Exception("�Ҳ������ݼ��ļ�" + path);
			}
		} else {
			throw new Exception("���ӵ���ŵ�ַ����Ϊ��");
		}
	}
	
	public void addClassify(String ics_id, TCComponent tcc) throws Exception {
		cls_manger.saveItemInNode(tcc, ics_id);
	}

	@Override
	public void onSingleError(int index, Exception e) {
		e.printStackTrace();
		
	}

	@Override
	public void onStart() {
		
	}

	@Override
	public void onFinish() {
		System.out.println("������ɣ�");
	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) {
		if (propertyDisplayName.equals("�ĵ����") || propertyDisplayName.equals("�汾") 
			||propertyDisplayName.equals("�ĵ�����") || propertyDisplayName.equals("�ĵ�����ID")
			|| propertyDisplayName.equals("���ӵ���ŵ�ַ")){
			return true;
		}
		return false;
	}

	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) {
		
		
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) {
		System.out.println("��" + index + "��");
		
	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) {
		e.printStackTrace();
		
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
