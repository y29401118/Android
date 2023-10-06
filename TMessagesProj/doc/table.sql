 --定时消息
 scheduled_messages (
    mid        INTEGER PRIMARY KEY '消息id',
    uid        INTEGER   '用户id',
    send_state INTEGER  '发送状态',
    date       INTEGER   '发送时间',
    data       BLOB,
    ttl        INTEGER,
    replydata  BLOB
);

--普通消息
messages (
    mid             INTEGER PRIMARY KEY  '消息id',
    uid             INTEGER   '用户id',
    read_state      INTEGER  '已读标识',
    send_state      INTEGER,  '发送状态标识',
    date            INTEGER,   '发送时间',
    data            BLOB,
    out             INTEGER,
    ttl             INTEGER,
    media           INTEGER,
    replydata       BLOB,
    imp             INTEGER,
    mention         INTEGER  '@消息标识',
    forwards        INTEGER   '转发标识',
    replies_data    BLOB,
    thread_reply_id INTEGER
);

--聊天对话框
dialogs (
    did            INTEGER PRIMARY KEY   '对话框id',
    date           INTEGER     '创建日期',
    unread_count   INTEGER     '消息未读数',
    last_mid       INTEGER,     '最后一条消息id',
    inbox_max      INTEGER,     '收件箱最大阅读id',
    outbox_max     INTEGER,     '发件箱最大阅读id',
    last_mid_i     INTEGER,     
    unread_count_i INTEGER     '@消息未读数',
    pts            INTEGER,    '同步消息序号，同步递增，用来判断是否有遗漏消息 从而进行同步'
    date_i         INTEGER,
    pinned         INTEGER,    '置顶标识'
    flags          INTEGER,     
    folder_id      INTEGER,   '对应的文件夹id'
    data           BLOB
);

--聊天对话文件夹
dialog_filter (
    id           INTEGER PRIMARY KEY,
    ord          INTEGER  '排序',   
    unread_count INTEGER  '文件夹未读消息数',
    flags        INTEGER,  '文件夹类型标识，例如联系人文件夹 群文件夹 频道文件夹 等等'
    title        TEXT     '文件夹名字',
);

--群聊
chats (
    uid  INTEGER PRIMARY KEY,  '群聊id'
    name TEXT,   '群名称'
    data BLOB,   '群信息（打包成二进制）'
);

--私聊
users (
    uid    INTEGER PRIMARY KEY,  '私聊id'
    name   TEXT,                  '私聊对方的名称'
    status INTEGER,               '上线时间'
    data   BLOB                    '私聊信息（打包成二进制）'
);


