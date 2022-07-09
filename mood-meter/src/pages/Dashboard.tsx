import NavigationBar from "../components/NavigationBar";
import { Link } from "react-router-dom";
import MoodDiagram from "./TestDiagram";
import { useEffect, useState } from "react";

export default function DashboardView() {
  const [questions, setQuestions] = useState();

  useEffect(() => {
    getQuestionsData();
  }, []);

  const getQuestionsData = async () => {
    const response = await fetch("/questions").then((response) =>
      response.json()
    );
    setQuestions(response);
  };

  if (questions) {
    const latestAnswerIndex = Object.keys(questions[0]["answers"]).length - 1;
    const latestAnswerDate = new Date(
      questions[0]["answers"][latestAnswerIndex]["date"]
    );
    const todayDate = new Date();
    const todayWasAnswered =
      latestAnswerDate.getDate() === todayDate.getDate() &&
      latestAnswerDate.getMonth() === todayDate.getMonth() &&
      latestAnswerDate.getFullYear() === todayDate.getFullYear();

    return (
      <NavigationBar>
        <div className="flex justify-center">
          <MoodDiagram width={500} height={500} question={questions[0]} />
        </div>

        <div className="flex justify-center">
          <button
            className={`btn btn-primary mx-auto ${
              todayWasAnswered && "btn-disabled"
            }`}
          >
            <Link to={`rate/${questions[0]["id"]}`}>Eintrag erstellen</Link>
          </button>
        </div>
      </NavigationBar>
    );
  } else {
    return <></>;
  }
}
