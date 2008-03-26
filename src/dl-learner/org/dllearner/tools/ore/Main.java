package org.dllearner.tools.ore;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;



public class Main {
    
    public static void main(String[] args) {
    	try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Wizard wizard = new Wizard();
        wizard.getDialog().setTitle("DL-Learner ORE-Tool");
        wizard.getDialog().setSize(700, 400);
        
        WizardPanelDescriptor descriptor1 = new IntroductionPanelDescriptor();
        wizard.registerWizardPanel(IntroductionPanelDescriptor.IDENTIFIER, descriptor1);

        WizardPanelDescriptor descriptor2 = new KnowledgeSourcePanelDescriptor();
        wizard.registerWizardPanel(KnowledgeSourcePanelDescriptor.IDENTIFIER, descriptor2);

        WizardPanelDescriptor descriptor3 = new ConceptPanelDescriptor();
        wizard.registerWizardPanel(ConceptPanelDescriptor.IDENTIFIER, descriptor3);
        
        WizardPanelDescriptor descriptor4 = new LearningPanelDescriptor();
        wizard.registerWizardPanel(LearningPanelDescriptor.IDENTIFIER, descriptor4);
        
        wizard.setCurrentPanel(IntroductionPanelDescriptor.IDENTIFIER);
        
        int ret = wizard.showModalDialog();
        
        System.out.println("Dialog return code is (0=Finish,1=Cancel,2=Error): " + ret);
       
        
        System.exit(0);
        
    }
    
}
