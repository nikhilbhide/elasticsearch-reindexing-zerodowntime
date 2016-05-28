package com.esreindexer.elasticsearch.client.reindex.json.entities;
/**
 * POJO class for ReindexServiceResponse Object. Used for _reindex api.
 * 
 * @author nikhil.bhide
 * @since 1.0
 * 
 */
public class ReindexServiceResponse {
    private String[] failures;
    private String created;
    private String updated;
    private String batches;
    private String took;
    private String version_conflicts;

    public String[] getFailures() {
        return failures;
    }

    public void setFailures(String[] failures) {
        this.failures = failures;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

	/**
	 * @return the batches
	 */
	public String getBatches() {
		return batches;
	}

	/**
	 * @param batches the batches to set
	 */
	public void setBatches(String batches) {
		this.batches = batches;
	}

	/**
	 * @return the took
	 */
	public String getTook() {
		return took;
	}

	/**
	 * @param took the took to set
	 */
	public void setTook(String took) {
		this.took = took;
	}

	/**
	 * @return the version_conflicts
	 */
	public String getVersion_conflicts() {
		return version_conflicts;
	}

	/**
	 * @param version_conflicts the version_conflicts to set
	 */
	public void setVersion_conflicts(String version_conflicts) {
		this.version_conflicts = version_conflicts;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(String updated) {
		this.updated = updated;
	}
}