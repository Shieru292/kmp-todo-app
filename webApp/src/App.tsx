import {fetchAllToDo as _fetchAllToDo , addToDo, ToDo, validateToDoJs} from "shared"
import {Suspense, use, useState} from "react";

const fetchAllToDo = _fetchAllToDo()

function ToDoList() {
    const todos = use(fetchAllToDo)
    const [nextId, setNextId] = useState(todos.length + 1)
    const [inputValue, setInputValue] = useState("")
    const [toDoList, setToDoList] = useState<ToDo[]>([...todos])

    const handleAddToDo = async () => {
        if (inputValue.trim() === "") return

        try {
            const newToDo = new ToDo(nextId, inputValue)
            const validationResult = validateToDoJs(newToDo)
            if (!validationResult.isValid) {
                alert(validationResult.errorMessage)
                return
            }
            await addToDo(newToDo)
            setToDoList([...toDoList, newToDo])
            setNextId(nextId + 1)
            setInputValue("")
        } catch (error) {
            console.error("Failed to add ToDo:", error)
        }
    }

    return (
        <div style={{ padding: "20px", fontFamily: "Arial" }}>
            <h1>📝 シンプルToDoアプリ</h1>

            <form
                onSubmit={(e) => {
                    e.preventDefault()
                    handleAddToDo()
                }}
                style={{ marginBottom: "20px" }}
            >
                <input
                    type="text"
                    value={inputValue}
                    onChange={(e) => setInputValue(e.target.value)}
                    placeholder="新しいToDoを入力..."
                    style={{
                        padding: "8px",
                        width: "300px",
                        fontSize: "14px"
                    }}
                />
                <button
                    type="submit"
                    style={{
                        padding: "8px 16px",
                        marginLeft: "10px",
                        cursor: "pointer",
                        backgroundColor: "#4CAF50",
                        color: "white",
                        border: "none",
                        borderRadius: "4px"
                    }}
                >
                    追加
                </button>
            </form>

            <ul style={{ listStyle: "none", padding: 0 }}>
                {toDoList.map((todo) => (
                    <li
                        key={todo.id}
                        style={{
                            padding: "12px",
                            marginBottom: "8px",
                            backgroundColor: "#f0f0f0",
                            borderLeft: "4px solid #4CAF50",
                            borderRadius: "4px"
                        }}
                    >
                        <strong>ID: {todo.id}</strong> - {todo.content}
                    </li>
                ))}
            </ul>

            {toDoList.length === 0 && (
                <p style={{ color: "#999" }}>ToDoはまだありません</p>
            )}
        </div>
    )
}

export default function App() {
    return (
        <Suspense fallback="Loading...">
            <ToDoList/>
        </Suspense>
    )
}