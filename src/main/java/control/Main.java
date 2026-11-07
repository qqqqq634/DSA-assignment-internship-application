package control;

import boundary.MainUI;
import utility.MessageUI;

public class Main {
    private ApplicantManager applicantManager = new ApplicantManager();
    private CompanyManager companyManager = new CompanyManager();
    private MainUI mainUI = new MainUI();

    /** Routes the main menu to applicant or company profiles until choice 3. */
    public void runMain() {
        int choice = 0;
        do {
            choice = mainUI.mainMenuInput();
            switch (choice) {
                case 1:
                    applicantManager.runApplicantProfile();
                    break;
                case 2:
                    companyManager.runCompanyProfile();
                    break;
                default:
                    MessageUI.displayInvalidChoiceMessage();
            }
        } while (choice != 3);
    }
}
