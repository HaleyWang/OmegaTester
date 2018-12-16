create table blog (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE  );
ALTER TABLE blog ADD name TEXT;

create table req_setting (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE  );
ALTER TABLE req_setting ADD name TEXT;
ALTER TABLE req_setting ADD onwer INTEGER;
ALTER TABLE req_setting ADD onwer_type TEXT;
ALTER TABLE req_setting ADD content TEXT;

create table req_account (account_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE  );
ALTER TABLE req_account ADD name TEXT;
ALTER TABLE req_account ADD email TEXT;
ALTER TABLE req_account ADD password TEXT;
ALTER TABLE req_account ADD akey TEXT;
ALTER TABLE req_account ADD token TEXT;
ALTER TABLE req_account ADD type TEXT;

create table req_batch_history (batch_history_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE  );
ALTER TABLE req_batch_history ADD batch_id INTEGER;
ALTER TABLE req_batch_history ADD name TEXT;
ALTER TABLE req_batch_history ADD batch_start_date TEXT;

create table req_batch (batch_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE  );
ALTER TABLE req_batch ADD schedule_id INTEGER;
ALTER TABLE req_batch ADD env_setting_id INTEGER;
ALTER TABLE req_batch ADD name TEXT;
ALTER TABLE req_batch ADD time_expression TEXT;
ALTER TABLE req_batch ADD statuts TEXT;
ALTER TABLE req_batch ADD created_on TEXT;
ALTER TABLE req_batch ADD updated_on TEXT;

create table req_group (group_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE  );
ALTER TABLE req_group ADD parent_id INTEGER;
ALTER TABLE req_group ADD name TEXT;
ALTER TABLE req_group ADD sort TEXT;

create table req_info (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE  );
ALTER TABLE req_info ADD name TEXT;
ALTER TABLE req_info ADD swagger_id TEXT;
ALTER TABLE req_info ADD url TEXT;
ALTER TABLE req_info ADD sort TEXT;
ALTER TABLE req_info ADD created_on TEXT;
ALTER TABLE req_info ADD updated_on TEXT;

create table req_meta (meta_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE  );
ALTER TABLE req_meta ADD data TEXT;

create table req_relation (relation_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE  );
ALTER TABLE req_relation ADD object_id INTEGER;
ALTER TABLE req_relation ADD type TEXT;
ALTER TABLE req_relation ADD permission TEXT;
ALTER TABLE req_relation ADD account_id INTEGER;

create table req_task_history (history_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE  );
ALTER TABLE req_task_history ADD batch_history_id INTEGER;
ALTER TABLE req_task_history ADD created_by_id INTEGER;
ALTER TABLE req_task_history ADD created_on TEXT;
ALTER TABLE req_task_history ADD test_statuts TEXT;
ALTER TABLE req_task_history ADD statuts TEXT;
ALTER TABLE req_task_history ADD statut_code TEXT;
ALTER TABLE req_task_history ADD test_success TEXT;

create table req_task_history_meta (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE  );
ALTER TABLE req_task_history_meta ADD content TEXT;
ALTER TABLE req_task_history_meta ADD test_report TEXT;

