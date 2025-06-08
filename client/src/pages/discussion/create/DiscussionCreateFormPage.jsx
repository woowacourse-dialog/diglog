import React, { useEffect, useState } from 'react';
import './DiscussionCreateFormPage.css';
import TitleInput from '../../../components/TitleInput/TitleInput';
import MarkdownEditorUiw from '../../../components/MarkdownEditor/MarkdownEditorUiw';
import Header from '../../../components/Header/Header';

const TRACKS = [
  { id: 'frontend', name: '프론트엔드' },
  { id: 'backend', name: '백엔드' },
  { id: 'android', name: '안드로이드' }
];

const DiscussionCreateFormPage = () => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [date, setDate] = useState('');
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');
  const [participantCount, setParticipantCount] = useState(2);
  const [location, setLocation] = useState('');
  const [track, setTrack] = useState('frontend');

  const handleSubmit = (e) => {
    e.preventDefault();

    const startDateTime = new Date(`${date}T${startTime}`).toISOString();
    const endDateTime = new Date(`${date}T${endTime}`).toISOString();
    console.log({ 
      title, 
      content, 
      startDateTime, 
      endDateTime,
      participantCount,
      location,
      track
    });
  };

  const handleParticipantCountChange = (e) => {
    const value = parseInt(e.target.value) || 2;
    setParticipantCount(Math.max(2, value));
  };

  const getDefaultDate = () => {
    const date = new Date();
    return date.toISOString().split('T')[0];
  };

  const getDefaultTime = (hoursToAdd = 0) => {
    const date = new Date();
    date.setHours(date.getHours() + hoursToAdd);
    // 현재 시간을 30분 단위로 반올림
    date.setMinutes(Math.ceil(date.getMinutes() / 30) * 30);
    return date.toTimeString().slice(0, 5); // HH:mm 형식
  };

  // 컴포넌트 마운트 시 기본값 설정
  useEffect(() => {
    setDate(getDefaultDate());
    setStartTime(getDefaultTime(1)); // 1시간 후
    setEndTime(getDefaultTime(2));   // 2시간 후
  }, []);

  return (
    <div className="discussion-create-page">
      <Header />
      <div className="discussion-create-container">
        <div className="discussion-create-form">
          <h1>새로운 토론 주제 작성</h1>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <TitleInput value={title} setTitle={setTitle} />
            </div>

            <div className="form-row">
              <div className="form-group flex-1">
                <label htmlFor="track">트랙</label>
                <select
                  id="track"
                  className="form-input"
                  value={track}
                  onChange={(e) => setTrack(e.target.value)}
                >
                  {TRACKS.map(t => (
                    <option key={t.id} value={t.id}>
                      {t.name}
                    </option>
                  ))}
                </select>
              </div>
              <div className="form-group flex-1">
                <label htmlFor="location">토론 장소</label>
                <input
                  type="text"
                  id="location"
                  className="form-input"
                  value={location}
                  onChange={(e) => setLocation(e.target.value)}
                  placeholder="예: 굿샷, 나이스샷, 온라인 줌 미팅"
                />
              </div>
              <div className="form-group participant-count">
                <label htmlFor="participantCount">참여자 수</label>
                <input
                  type="number"
                  id="participantCount"
                  className="form-input"
                  value={participantCount}
                  onChange={handleParticipantCountChange}
                  min="2"
                  placeholder="최소 2명"
                />
              </div>
            </div>

            <div className="datetime-container">
              <div className="date-field">
                <label htmlFor="date">날짜</label>
                <input
                  type="date"
                  id="date"
                  value={date}
                  onChange={(e) => setDate(e.target.value)}
                  min={getDefaultDate()}
                />
              </div>
              <div className="time-inputs">
                <div className="time-field">
                  <label htmlFor="startTime">시작 시간</label>
                  <input
                    type="time"
                    id="startTime"
                    value={startTime}
                    onChange={(e) => setStartTime(e.target.value)}
                    step="1800" // 30분 단위
                  />
                </div>
                <div className="time-field">
                  <label htmlFor="endTime">종료 시간</label>
                  <input
                    type="time"
                    id="endTime"
                    value={endTime}
                    onChange={(e) => setEndTime(e.target.value)}
                    step="1800" // 30분 단위
                  />
                </div>
              </div>
            </div>

            <div className="form-group">
              <MarkdownEditorUiw value={content} onChange={setContent} />
            </div>

            <div className="discussion-form-actions">
              <button type="button" className="discussion-button discussion-button-cancel">취소</button>
              <button type="submit" className="discussion-button discussion-button-submit">등록</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default DiscussionCreateFormPage;