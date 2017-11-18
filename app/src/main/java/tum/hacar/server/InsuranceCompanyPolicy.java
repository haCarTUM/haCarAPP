package tum.hacar.server;

/**
 * the company, the boss
 *
 * @author Matthias Kammueller
 */
public abstract class InsuranceCompanyPolicy {
    /**
     * after a incident we have to decide how much the customer gets out of the insurance fonds
     * @param customer the user ID
     * @param info some info about his driving style etc
     * @return the amount of cents he gets back
     */
    public abstract int doesUserGetRefund(int customer, Object info);
}
