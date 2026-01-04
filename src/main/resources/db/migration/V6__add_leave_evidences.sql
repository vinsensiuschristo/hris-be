-- V6: Add leave_evidences table
CREATE TABLE IF NOT EXISTS leave_evidences (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    leave_request_id UUID NOT NULL REFERENCES leave_requests(id) ON DELETE CASCADE,
    file_path TEXT NOT NULL,
    file_type VARCHAR(100),
    uploaded_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_leave_evidences_leave_request_id ON leave_evidences(leave_request_id);
