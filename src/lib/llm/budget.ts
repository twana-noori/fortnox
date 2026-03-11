src/lib/llm/budget.ts
```typescript
class TokenBudgetManager {
    private budget: number;
    private usedTokens: number;

    constructor(initialBudget: number) {
        this.budget = initialBudget;
        this.usedTokens = 0;
    }

    public addTokens(tokens: number): void {
        this.budget += tokens;
    }

    public useTokens(tokens: number): boolean {
        if (this.canUseTokens(tokens)) {
            this.usedTokens += tokens;
            return true;
        }
        return false;
    }

    public canUseTokens(tokens: number): boolean {
        return this.usedTokens + tokens <= this.budget;
    }

    public getRemainingTokens(): number {
        return this.budget - this.usedTokens;
    }

    public resetBudget(): void {
        this.usedTokens = 0;
    }
}

export default TokenBudgetManager;