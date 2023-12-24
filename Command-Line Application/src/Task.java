class Task {

    private String description;
    private boolean complete;


    public Task(String description) {
        this.description = description;
        this.complete = false;
    }

    public Task(String description, boolean complete) {
        this.description = description;
        this.complete = complete;
    }


    public String getDescription() {
        return description;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }


}




