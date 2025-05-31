import React from 'react';
import './DiscussionCreateFormPage.css';
import TitleInput from '../../../components/TitleInput/TitleInput';

const DiscussionCreateFormPage = () => { 

  const onChangeTitle = (title) => {
    console.log(title);
  }
  return (
    <div className='wrapper'>
      <TitleInput onChangeTitle={onChangeTitle} />
    </div>
  );
};

export default DiscussionCreateFormPage;