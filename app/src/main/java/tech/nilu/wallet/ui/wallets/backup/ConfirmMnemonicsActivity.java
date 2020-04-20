package tech.nilu.wallet.ui.wallets.backup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.nilu.wallet.R;
import tech.nilu.wallet.ui.common.BaseActivity;

/**
 * Created by root on 2/6/18.
 */

public class ConfirmMnemonicsActivity extends BaseActivity {
    public static final String MNEMONIC = "Mnemonic";

    @BindView(R.id.mnemonicsList)
    RecyclerView mnemonicsList;
    @BindView(R.id.wordsList)
    RecyclerView wordsList;

    private WordsAdapter wordsAdapter;
    private MnemonicsAdapter mnemonicsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_mnemonics);
        ButterKnife.bind(this);

        initUI();

        String mnemonic = getIntent().getStringExtra(MNEMONIC);
        List<String> mnemonicWords = new ArrayList<>(Arrays.asList(mnemonic.split(" ")));
        Collections.shuffle(mnemonicWords);
        for (String word : mnemonicWords)
            wordsAdapter.add(wordsAdapter.getItemCount(), word);
    }

    @OnClick(R.id.confirmButton)
    public void onConfirmClick() {
        String mnemonic = getIntent().getStringExtra(MNEMONIC);
        StringBuilder mnemonicBuilder = new StringBuilder();
        int iterations = mnemonicsAdapter.getItems().size();
        for (int i = 0; i < iterations; i++) {
            mnemonicBuilder.append(mnemonicsAdapter.getItems().get(i));

            boolean notLastIteration = i < iterations - 1;
            if (notLastIteration) {
                mnemonicBuilder.append(" ");
            }
        }
        if (mnemonic.equals(mnemonicBuilder.toString())) {
            setResult(RESULT_OK);
            finish();
        } else
            Toast.makeText(this, getString(R.string.err_incorrect_mnemonic), Toast.LENGTH_SHORT).show();
    }

    private void initUI() {
        wordsAdapter = new WordsAdapter();
        wordsList.setLayoutManager(new GridLayoutManager(this, 4));
        wordsList.setAdapter(wordsAdapter);

        mnemonicsAdapter = new MnemonicsAdapter();
        mnemonicsList.setLayoutManager(new GridLayoutManager(this, 4));
        mnemonicsList.setAdapter(mnemonicsAdapter);
    }

    class MnemonicsAdapter extends RecyclerView.Adapter {
        private ArrayList<String> items = new ArrayList<>();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MnemonicsViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MnemonicsViewHolder)
                ((MnemonicsViewHolder) holder).bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void add(int pos, String item) {
            items.add(pos, item);
            notifyItemInserted(pos);
        }

        public void remove(int pos) {
            items.remove(pos);
            notifyItemRemoved(pos);
        }

        public ArrayList<String> getItems() {
            return items;
        }

        class MnemonicsViewHolder extends RecyclerView.ViewHolder {
            @BindView(android.R.id.text1)
            TextView text;

            MnemonicsViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mnemonic, parent, false));
                ButterKnife.bind(this, itemView);
            }

            public void bind(String item) {
                text.setText(item);
                itemView.setOnClickListener(v -> {
                    wordsAdapter.add(wordsAdapter.getItemCount(), item);
                    mnemonicsAdapter.remove(getAdapterPosition());
                });
            }
        }
    }

    class WordsAdapter extends RecyclerView.Adapter {
        private ArrayList<String> items = new ArrayList<>();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WordsViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof WordsViewHolder)
                ((WordsViewHolder) holder).bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void add(int pos, String item) {
            items.add(pos, item);
            notifyItemInserted(pos);
        }

        public void remove(int pos) {
            items.remove(pos);
            notifyItemRemoved(pos);
        }

        class WordsViewHolder extends RecyclerView.ViewHolder {
            @BindView(android.R.id.button1)
            Button button;

            WordsViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false));
                ButterKnife.bind(this, itemView);
            }

            public void bind(String item) {
                button.setText(item);
                itemView.setOnClickListener(v -> {
                    mnemonicsAdapter.add(mnemonicsAdapter.getItemCount(), item);
                    wordsAdapter.remove(getAdapterPosition());
                });
            }
        }
    }
}
