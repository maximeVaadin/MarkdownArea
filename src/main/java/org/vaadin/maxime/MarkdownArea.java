package org.vaadin.maxime;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

@Tag("markdown-area")
public class MarkdownArea extends Composite<Div> {

	private TextArea input = new TextArea();
	private Link link = new Link("Markdown","https://vaadin.com/markdown-guide");
	private Label label = new Label(" supported");
	private Div writeView = new Div(input, link, label);
	private Div previewView = new Div();
	private Tab writeTab = new Tab("Write");
	private Tab previewTab = new Tab("Preview");
	private Tabs tabs = new Tabs(writeTab, previewTab);
    Parser parser = Parser.builder().build();
    HtmlRenderer renderer = HtmlRenderer.builder().build();

	public MarkdownArea() {
		init();
	}

	public MarkdownArea(String text) {
		if (StringUtil.isNotBlank(text)) {
			setValue(text);
		}
		init();
	}

	private void init () {
		input.setWidth("100%");
		link.setTarget("_blank");
		previewView.setVisible(false);
		getContent().add(tabs, writeView, previewView);
		tabs.addSelectedChangeListener(event -> {
			if(tabs.getSelectedTab().getLabel().equals("Preview")) {
				writeView.setVisible(false);
				previewView.setVisible(true);
				String text = getValue().isEmpty() ? "*Nothing to preview*" : getValue();
				addMarkdown(text);
			} else {
				writeView.setVisible(true);
				previewView.setVisible(false);
			}
		});
	}

	private void addMarkdown(String value) {
		String html = String.format("<div>%s</div>",
				parseMarkdown(StringUtil.getNullSafeString(value)));
		Html item = new Html(html);
		previewView.removeAll();
		previewView.add(item);
	}

	private String parseMarkdown(String value) {
        Node text = parser.parse(value);
        return renderer.render(text);
    }

	public void setValue(String value) {
		input.setValue(value);
	}

	public String getValue() {
		return input.getValue();
	}

	public TextArea getInput() {
		return input;
	}

	public void setMarkdownLink (String markdownLink) {link.setHref(markdownLink);}

}


