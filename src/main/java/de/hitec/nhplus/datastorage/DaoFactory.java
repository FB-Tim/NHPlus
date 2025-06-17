package de.hitec.nhplus.datastorage;

public class DaoFactory {

    private static DaoFactory instance;


    /**
     * Factory class for creating DAO (Data Access Object) instances.
     * <p>
     * Implements the Singleton pattern to provide a single global instance
     * for creating DAO objects that share the same database connection.
     */
    private DaoFactory() {
    }

    /**
     * Returns the singleton instance of the DaoFactory.
     * If the instance does not exist, it is created.
     *
     * @return The singleton instance of DaoFactory.
     */
    public static DaoFactory getDaoFactory() {
        if (DaoFactory.instance == null) {
            DaoFactory.instance = new DaoFactory();
        }
        return DaoFactory.instance;
    }


    /**
     * Creates and returns a new TreatmentDao using a shared database connection.
     *
     * @return A new instance of TreatmentDao.
     */
    public TreatmentDao createTreatmentDao() {
        return new TreatmentDao(ConnectionBuilder.getConnection());
    }

    /**
     * Creates and returns a new PatientDao using a shared database connection.
     *
     * @return A new instance of PatientDao.
     */
    public PatientDao createPatientDAO() {
        return new PatientDao(ConnectionBuilder.getConnection());
    }


    /**
     * Creates and returns a new NurseDao using a shared database connection.
     *
     * @return A new instance of NurseDao.
     */
    public NurseDao createNurseDao() { return new NurseDao(ConnectionBuilder.getConnection()); }

    /**
     * Creates and returns a new AdminDao using a shared database connection.
     *
     * @return A new instance of AdminDao.
     */
    public AdminDao createAdminDao() {return new AdminDao(ConnectionBuilder.getConnection());}
}
