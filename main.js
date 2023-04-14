const game = () => {
    let pScore = 0;
    let cScore = 0;

    //Start the Game
    const startGame = () => {
        const playBtn = document.querySelector(".intro button");
        const introScreen = document.querySelector(".intro");
        const match = document.querySelector(".match");

        playBtn.addEventListener("click", () => {
            introScreen.classList.add("fadeOut");
            match.classList.add("fadeIn");
        });
    };
    //Play Match
    const playMatch = () => {
        const options = document.querySelectorAll(".options button");
        const playerHand = document.querySelector(".user1");
        const computerHand = document.querySelector(".user2");
        const hands = document.querySelectorAll(".hands img");


        //Computer Options
        const computerOptions = ["rock", "paper", "scissors"];

        options.forEach(option => {
            option.addEventListener("click", function() {
                //Computer Choice
                const computerNumber = Math.floor(Math.random() * 3);
                const computerChoice = computerOptions[computerNumber];

                setTimeout(() => {
                    //Here is where we call compare hands
                    compareHands(this.textContent, computerChoice);
                    //Update Images
                    playerHand.src = `images/${this.textContent}.png`;
                    computerHand.src = `images/${computerChoice}.png`;
                }, 2000);

            });
        });
    };

    const updateScore = () => {
        const playerScore = document.querySelector(".user1-score p");
        const computerScore = document.querySelector(".user2-score p");
        playerScore.textContent = pScore;
        computerScore.textContent = cScore;
    };

    const compareHands = (playerChoice, computerChoice) => {
        //Update Text
        const winner = document.querySelector(".winner");
        //Checking for a tie
        if (playerChoice === computerChoice) {
            winner.textContent = "It is a tie";
            return;
        }
        //Check for Rock
        if (playerChoice === "rock") {
            if (computerChoice === "scissors") {
                winner.textContent = "User 1 Wins";
                pScore++;
                updateScore();
                return;
            } else {
                winner.textContent = "User 2 Wins";
                cScore++;
                updateScore();
                return;
            }
        }
        //Check for Paper
        if (playerChoice === "paper") {
            if (computerChoice === "scissors") {
                winner.textContent = "User 1 Wins";
                cScore++;
                updateScore();
                return;
            } else {
                winner.textContent = "User 2 Wins";
                pScore++;
                updateScore();
                return;
            }
        }
        //Check for Scissors
        if (playerChoice === "scissors") {
            if (computerChoice === "rock") {
                winner.textContent = "User 2  Wins";
                cScore++;
                updateScore();
                return;
            } else {
                winner.textContent = "User 1 Wins";
                pScore++;
                updateScore();
                return;
            }
        }
    };

    //Is call all the inner function
    startGame();
    playMatch();
};

//start the game function
game();