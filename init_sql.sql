create table blog (id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL  );
ALTER TABLE blog ADD name VARCHAR(2048);

create table req_setting (id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL  );
ALTER TABLE req_setting ADD name VARCHAR(50);
ALTER TABLE req_setting ADD onwer BIGINT;
ALTER TABLE req_setting ADD current INTEGER;
ALTER TABLE req_setting ADD onwer_type VARCHAR(50);
ALTER TABLE req_setting ADD content CLOB;
ALTER TABLE req_setting ADD type VARCHAR(50);

create table req_account (account_id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL  );
ALTER TABLE req_account ADD name VARCHAR(50);
ALTER TABLE req_account ADD email VARCHAR(50);
ALTER TABLE req_account ADD password VARCHAR(50);
ALTER TABLE req_account ADD akey VARCHAR(50);
ALTER TABLE req_account ADD token VARCHAR(50);
ALTER TABLE req_account ADD type VARCHAR(50);

create table req_batch_history (batch_history_id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL  );
ALTER TABLE req_batch_history ADD batch_id BIGINT;
ALTER TABLE req_batch_history ADD name VARCHAR(50);
ALTER TABLE req_batch_history ADD batch_start_date TIMESTAMP;

create table req_batch (batch_id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL  );
ALTER TABLE req_batch ADD schedule_id BIGINT;
ALTER TABLE req_batch ADD env_setting_id BIGINT;
ALTER TABLE req_batch ADD group_id BIGINT;
ALTER TABLE req_batch ADD created_by_id BIGINT;
ALTER TABLE req_batch ADD modified_by_id BIGINT;
ALTER TABLE req_batch ADD name VARCHAR(50);
ALTER TABLE req_batch ADD enable BOOLEAN;
ALTER TABLE req_batch ADD time_expression VARCHAR(50);
ALTER TABLE req_batch ADD statuts VARCHAR(50);
ALTER TABLE req_batch ADD created_on TIMESTAMP;
ALTER TABLE req_batch ADD updated_on TIMESTAMP;

create table req_group (group_id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL  );
ALTER TABLE req_group ADD parent_id BIGINT;
ALTER TABLE req_group ADD created_by_id BIGINT;
ALTER TABLE req_group ADD modified_by_id BIGINT;
ALTER TABLE req_group ADD name VARCHAR(50);
ALTER TABLE req_group ADD sort INTEGER;

create table req_info (id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL  );
ALTER TABLE req_info ADD name VARCHAR(50);
ALTER TABLE req_info ADD swagger_id VARCHAR(50);
ALTER TABLE req_info ADD url VARCHAR(2048);
ALTER TABLE req_info ADD method VARCHAR(50);
ALTER TABLE req_info ADD group_id BIGINT;
ALTER TABLE req_info ADD sort INTEGER;
ALTER TABLE req_info ADD created_on TIMESTAMP;
ALTER TABLE req_info ADD updated_on TIMESTAMP;

create table req_meta (meta_id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL  );
ALTER TABLE req_meta ADD req_id BIGINT;
ALTER TABLE req_meta ADD data_type VARCHAR(50);
ALTER TABLE req_meta ADD data CLOB;

create table req_relation (relation_id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL  );
ALTER TABLE req_relation ADD object_id BIGINT;
ALTER TABLE req_relation ADD type VARCHAR(50);
ALTER TABLE req_relation ADD permission VARCHAR(50);
ALTER TABLE req_relation ADD account_id BIGINT;

create table req_task_history (task_history_id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL  );
ALTER TABLE req_task_history ADD batch_history_id BIGINT;
ALTER TABLE req_task_history ADD req_id BIGINT;
ALTER TABLE req_task_history ADD his_type VARCHAR(50);
ALTER TABLE req_task_history ADD created_by_id BIGINT;
ALTER TABLE req_task_history ADD created_on TIMESTAMP;
ALTER TABLE req_task_history ADD test_statuts VARCHAR(50);
ALTER TABLE req_task_history ADD statuts VARCHAR(50);
ALTER TABLE req_task_history ADD statut_code VARCHAR(50);
ALTER TABLE req_task_history ADD test_success VARCHAR(50);

create table req_task_history_meta (id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL  );
ALTER TABLE req_task_history_meta ADD task_history_id BIGINT;
ALTER TABLE req_task_history_meta ADD content CLOB;
ALTER TABLE req_task_history_meta ADD test_report VARCHAR(4000);

