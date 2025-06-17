package de.hitec.nhplus.datastorage;

public class DaoFactory {

    private static DaoFactory instance;

    private DaoFactory() {
    }

    public static DaoFactory getDaoFactory() {
        if (DaoFactory.instance == null) {
            DaoFactory.instance = new DaoFactory();
        }
        return DaoFactory.instance;
    }

    public TreatmentDao createTreatmentDao() {
        return new TreatmentDao(ConnectionBuilder.getConnection());
    }

    public ArchiveDao createArchiveDao() {
        return new ArchiveDao(ConnectionBuilder.getConnection());
    }

    public ArchivePatientDao createArchivePatientDao() {
        return new ArchivePatientDao(ConnectionBuilder.getConnection());
    }


    public PatientDao createPatientDAO() {
        return new PatientDao(ConnectionBuilder.getConnection());
    }

    public NurseDao createNurseDao() { return new NurseDao(ConnectionBuilder.getConnection()); }

    public AdminDao createAdminDao() {return new AdminDao(ConnectionBuilder.getConnection());}
}
