package com.forgqi.resourcebaseserver.entity.forum;

public interface IVoteEntity {
    Integer getUpVote();

    void setUpVote(Integer upVote);

    Integer getDownVote();

    void setDownVote(Integer downVote);

    abstract class VoteEntityAdapter implements IVoteEntity{

        @Override
        public Integer getUpVote() {
            return null;
        }

        @Override
        public void setUpVote(Integer upVote) {

        }

        @Override
        public Integer getDownVote() {
            return null;
        }

        @Override
        public void setDownVote(Integer downVote) {

        }
    }
}