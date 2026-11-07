package entity;
import java.io.Serializable;
public class Match implements Serializable {
    private Student student;
    private JobPosting job;
    private double score;

    /**
     * Creates a match holding the supplied student and job references and score.
     * @param student the student value
     * @param job the job value
     * @param score the score value
     */
    public Match(Student student, JobPosting job, double score) {
        this.student = student;
        this.job = job;
        this.score = score;
    }

    /**
     * Copies the score and shares the student and job references.
     * @param match the match value
     */
    public Match(Match match) {
        this.student = match.getStudent();
        this.job = match.getJob();
        this.score = match.getScore();
    }   

    /**
     * Returns the student.
     * @return returns the student.
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Sets the student.
     * @param student the student value
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * Returns the job.
     * @return returns the job.
     */
    public JobPosting getJob() {
        return job;
    }

    /**
     * Sets the job.
     * @param job the job value
     */
    public void setJob(JobPosting job) {
        this.job = job;
    }

    /**
     * Returns the score.
     * @return returns the score.
     */
    public double getScore() {
        return score;
    }

    /**
     * Sets the score.
     * @param score the score value
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Returns the job posting.
     * @return returns the job posting.
     */
    public JobPosting getJobPosting() {
        return job;
    }

    /**
     * Formats this entity and its relevant details for display.
     * @param i the i value
     * @return formats this entity and its relevant details for display.
     */
    public String toString(int i) {
        return "\n=========================\n"+"Match "+ i +"\n" +"=========================\n"+
                "=======Student=====\n" + student +
                "=======Job========\n"+ job +"=======Score======\n"+ score;
    }

        /**
     * Formats this entity and its relevant details for display.
     * @return formats this entity and its relevant details for display.
     */

    @Override
    public String toString() {
        return "\n=========================\n"+"Match\n" +"=========================\n"+
                "=======Student=====\n" + student +
                "=======Job========\n"+ job +"=======Score======\n"+ score;
    }
}
