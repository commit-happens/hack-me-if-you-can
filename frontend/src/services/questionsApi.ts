import emailsData from "../data/emails.json";
import apiClient from "./apiClient";

const USE_MOCK_QUESTIONS = true;

export const getQuestions = async () => {
  try {
    if (USE_MOCK_QUESTIONS) {
      return new Promise((resolve) => {
        setTimeout(() => {
          resolve(emailsData.emails);
        }, 1000);
      });
    }

    const response = await apiClient.get("/api/questions?limit=20&random=true");

    return response.data;
  } catch (error) {
    console.error("Failed to fetch questions:", error);
    throw error;
  }
};